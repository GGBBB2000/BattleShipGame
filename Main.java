import java.util.Scanner;
class Main {
    static int order = 0;//先行なら0
    static int map[][] = new int [10][10];
    static int enemyMap[][] = new int [10][10];
    static int searchMap[][] = new int [10][10];
    //static int mode = 0;
    Fleet CV = new Fleet(2,2,0,5);
    Fleet BattleShip = new Fleet(4,4,0,4);
    Fleet Cruiser1 = new Fleet(5,5,1,3);
    Fleet Cruiser2 = new Fleet(6,6,0,3);
    Fleet Destroyer = new Fleet(7,7,0,2);

    public static void main (String args[]){
        initialize();
        if (order==0){
            deffense();
        }
        do{
            attack();
            deffense();
        }while(judge());
    }

    static void initialize(){
        put(2,2,0,5);
        put(4,4,0,4);
        put(5,5,1,3);
        put(6,6,0,3);
        put(7,7,0,2);
        printMap();
    }
    static boolean judge(){
        return false;
    }
    static void attack(){
            enemyMap[4][4]=1;
            count(2);
            count(3);
            count(3);
            count(4);
            count(5);
            printSearchMap();
            check();
    }
    static void deffense(){
            check();
    }
    static void check(){
        
    }
    static void put(int x,int y,int dir,int length){
        for(int i =0;i<length;i++){
            if (dir==0){
                map[x+i][y]=1;
            }
            if (dir==1){
                map[x][y+i]=1;
            }
        }
    }
    static void printMap(){
        for(int i= 0;i<10;i++){
            for (int j=0;j<10;j++){
                System.out.print(""+map[j][i]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    static void printSearchMap(){
        for(int i= 0;i<10;i++){
            for (int j=0;j<10;j++){
                System.out.print(""+searchMap[j][i]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    static void count(int length){
        for(int i=0;i+length<=10;i++){//横向き
            for (int j=0;j<10;j++){
                boolean boo = true;
                for(int k=0;k<length;k++){
                    if (enemyMap[i+k][j]!=0){
                        boo = false;
                    }
                }
                if (boo){
                    for(int k=0;k<length;k++){
                        searchMap[i+k][j]++;
                    }
                }

            }
        }
        for(int i=0;i<10;i++){//縦向き
            for (int j=0;j+length<=10;j++){
                boolean boo = true;
                for(int k=0;k<length;k++){
                    if (enemyMap[i][j+k]!=0){
                        boo = false;
                    }
                }
                if (boo){
                    for(int k=0;k<length;k++){
                        searchMap[i][j+k]++;
                    }
                }

            }
        }
    }
}
