import java.util.Scanner;
class Main {
    static int order = 0;//先行なら0
    static int map[][] = new int [10][10];//自分のマップ
    static int enemyMap[][] = new int [10][10];//敵のマップ
    static int searchMap[][] = new int [10][10];//敵を探索するときのマップ
    //static int mode = 0;
    Fleet CV = new Fleet(2,2,0,5);//船の場所を自動生成するメソッドを作るまではこれで行く
    Fleet BattleShip = new Fleet(4,4,0,4);//もしかしたらこんなのいらないかもしれない
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
        put(2,2,0,5);//空母
        put(4,4,0,4);//戦艦
        put(5,5,1,3);//巡洋艦1
        put(6,6,0,3);//巡洋艦2
        put(7,7,0,2);//駆逐艦
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
        for(int i =0;i<length;i++){//長さの分だけ置く
            if (dir==0){//横向きに置く
                map[x+i][y]=1;
            }
            if (dir==1){//縦向きに置く
                map[x][y+i]=1;
            }
        }
    }
    static void printMap(){//自分のマップ表示
        for(int i= 0;i<10;i++){
            for (int j=0;j<10;j++){
                System.out.print(""+map[j][i]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    static void printSearchMap(){//敵の探索用のマップ表示
        for(int i= 0;i<10;i++){
            for (int j=0;j<10;j++){
                System.out.print(""+searchMap[j][i]+" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    static void count(int length){//置くことができる個数を調べるメソッド
        for(int i=0;i+length<=10;i++){//横向きで置くことを考える。ただしはみ出さないようにする
            for (int j=0;j<10;j++){
                boolean boo = true;
                for(int k=0;k<length;k++){//置き場所から長さの分をみる
                    if (enemyMap[i+k][j]!=0){//空かどうかをみる
                        boo = false;
                    }
                }
                if (boo){//すべて空の場合
                    for(int k=0;k<length;k++){//おいた場所全てに1を足す
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
