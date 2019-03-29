import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class Main {

	public static void main(String[] args) {
		
        ArrayList<Points> FullList=new ArrayList<Points>(SetPoints());
        SortListBaseX(FullList);
        
        long nano_startTime = System.nanoTime(); //start time
        ArrayList<Points> ResultList=new ArrayList<Points>();
        findResultList(ResultList,FullList);

        
        ArrayList<Points> Result=new ArrayList<Points>();
        for(int i=0; i<ResultList.size();i++){
        	if(!Result.contains(ResultList.get(i)))
        		Result.add(ResultList.get(i));
        }
        
        System.out.println("total "+(FullList.size()+2)+" points.");
        System.out.println("There are "+Result.size()+" points on the convexhull, they are:");
        for(int i=0;i<Result.size();i++){
        	System.out.println(Result.get(i));
        }
        long nano_endTime = System.nanoTime(); //End time of execution
        System.out.println("Time taken in nano seconds to execute the code: " + (nano_endTime - nano_startTime));
	}
    
	//Input points
	public static ArrayList<Points> SetPoints(){
        ArrayList<Points> TotalList=new ArrayList<Points>();       
        TotalList.add(new Points(0,0));
        TotalList.add(new Points(0,4));
        TotalList.add(new Points(-4,0));
        TotalList.add(new Points(5,0));
        TotalList.add(new Points(0,-6));
        TotalList.add(new Points(1,0));

		return TotalList;	
	}
	
	//Sorting a point array based on the value of X-coodinator.
	public static ArrayList<Points> SortListBaseX (ArrayList<Points> SortList){
		Comparator<Points> comparator = new Comparator<Points>(){
			
			public int compare(Points a, Points b) {
				if (a.x-b.x!=0){
					return a.x-b.x;
				}
				else{
				    return a.y-b.y;					
				}
			}
		};
        Collections.sort(SortList,comparator);
		return SortList;
	}
	
	//Find ConvexHull based on D&C
	public static void FindConvexHull(Points a, Points b, ArrayList<Points> SideList, ArrayList<Points> ResultList){
		 Iterator<Points> it = SideList.iterator();
		 ArrayList<Points> LeftSideList=new ArrayList<Points>();
		 ArrayList<Points> RightSideList=new ArrayList<Points>();
		 
		 Points MaxPoint=null;
		 int max=0;
		 
		 while(it.hasNext()){
			 Points temp=it.next();			 
			 double compute= cross(temp,a,b);
		     
		     if (compute>max){
		    	 MaxPoint=temp;
		     }
		 }
		 
		 if (MaxPoint!=null){
			 ResultList.add(MaxPoint);
			 SideList.remove(MaxPoint);
			 Iterator<Points> it2 = SideList.iterator();
			 while (it2.hasNext()){
				 Points temp=it2.next();
				 if (onLeft(temp,a,MaxPoint)==1){
					 LeftSideList.add(temp);
				 }
				 if (onLeft(temp,MaxPoint,b)==1){
					 RightSideList.add(temp);
				 }
			 }
			 FindConvexHull(a,MaxPoint,LeftSideList,ResultList);
			 FindConvexHull(MaxPoint,b,RightSideList,ResultList);
		 }	 
	}

	//Devide the whole dot set as two parts.
	public static ArrayList<Points> SideList(Points a, Points b, ArrayList<Points> TotalList,String side){
		
		Iterator<Points> it = TotalList.iterator();
		ArrayList<Points> LeftList=new ArrayList<Points>();
		ArrayList<Points> RightList=new ArrayList<Points>();
		
		while(it.hasNext()){
			Points temp=it.next();
			if (onLeft(temp,a,b)==1){
				LeftList.add(temp);
			}
			else if (onLeft(temp,a,b)==2){
				RightList.add(temp);
			}
		}
		if (side.equals("left")){
			return LeftList;
		}
		else if (side.equals("right")){
			return RightList;
		}
		else{
			return null;
		}
	}

	//Determine if the point is on the left/right of the line, if left is 1, right is 2, on the line is 3.
	public static int onLeft(Points target,Points p1,Points p2){
        
		double compute=cross(target,p1,p2);
		
        if(compute > 0)
            return 1;//left
        else if(compute < 0)
            return 2;//right
        else
        	return 3;//on the line
    }
	
	//Compute the two edges of a triangle based on formula.
	public static double cross(Points target, Points p1, Points p2){
		int x1 = p1.x,y1 = p1.y;
        int x2 = p2.x,y2 = p2.y;
        int x3 = target.x,y3 = target.y;     
        int compute = x1*y2 + x3*y1 + x2*y3 - x3*y2 - x2*y1 - x1*y3;
        return compute;
	}
	//Method of getting the result list. only consider left point set and right 
	public static ArrayList<Points> findResultList (ArrayList<Points> ResultList,ArrayList<Points> FullList){
		
		ResultList.add(FullList.get(0));
        ResultList.add(FullList.get(FullList.size()-1));
        FullList.remove(0);
        FullList.remove(FullList.size()-1);
        
		ArrayList<Points> LeftList=new ArrayList<Points>(SideList(ResultList.get(0),ResultList.get(1),FullList,"left"));//Left Side List
		FindConvexHull(ResultList.get(0),ResultList.get(1),LeftList,ResultList);
		
        ArrayList<Points> RightList=new ArrayList<Points>(SideList(ResultList.get(0),ResultList.get(1),FullList,"right"));//Right Side List
        FindConvexHull(ResultList.get(1),ResultList.get(0),RightList,ResultList);
		
		return ResultList;
	}


}