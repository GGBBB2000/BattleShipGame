import java.util.Scanner;
class Main {
    static int order = 1;//先行なら0
    static int map[][] = new int [10][10];//自分のマップ
    static int enemyMap[][] = new int [10][10];//敵のマップ
    static int searchMap[][] = new int [10][10];//敵を探索するときのマップ
    //static int mode = 0;
    Fleet CV = new Fleet(2,2,0,5);//船の場所を自動生成するメソッドを作るまではこれで行く
    Fleet BattleShip = new Fleet(4,4,0,4);//もしかしたらこんなのいらないかもしれない
    Fleet Cruiser1 = new Fleet(5,5,1,3);
    Fleet Cruiser2 = new Fleet(6,6,0,3);
    Fleet Destroyer = new Fleet(7,7,0,2);
    static int hp [] = {5,2,3,3,4,5};//船のIDに合わせて。index0番めは残りの船の数

    public static void main (String args[]){
        initialize();
        if (order==1){
            deffense();
        }
        
        while(true){
            if (!attack()){
                return;
            }
            if(!deffense()){
                return;
            }
        }
    }

    static void initialize(){
        put(2,2,0,5,5);//空母のIDは5
        put(4,4,0,4,4);//戦艦のIDは4
        put(5,5,1,3,3);//巡洋艦1のIDは3
        put(6,6,0,3,2);//巡洋艦2のIDは2
        put(7,7,0,2,1);//駆逐艦のIDは1
        printMap();
    }
    static boolean judge(){
        return false;
    }
    static boolean attack(){
            enemyMap[4][4]=1;
            countClear();
            count(2);//ここなんかダサい
            count(3);
            count(3);
            count(4);
            count(5);
            printSearchMap();
            choose();
            return true;
    }
    static boolean deffense(){
        boolean boo = true;
        System.out.println("敵の攻撃\nスペース区切りで座標を入力\n左上は1,1");
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt()+1;
        int y = sc.nextInt()+1;
        if (map[x][y]>0){
            int i = map[x][y];
            map[x][y]=0;
            //map[x][y]*=-1;//こっちやったら書式崩れたので
            hp[i]--;
            if (hp[i]==0){
                System.out.println("撃沈");
                hp[0]--;
                if (hp[0]==0){
                    System.out.println("負け");
                    boo = false;
                }
            }else {
                System.out.println("Hit!!");
            }
        }else{
            System.out.println("ハズレ");
        }
        printMap();
        return boo;
    }
    static void check(){
        
    }
    static void put(int x,int y,int dir,int length,int shipType){
        for(int i =0;i<length;i++){//長さの分だけ置く
            if (dir==0){//横向きに置く
                map[x+i][y]=shipType;
            }
            if (dir==1){//縦向きに置く
                map[x][y+i]=shipType;
            }
        }
    }
    static void printMap(){//自分のマップ表示
        System.out.print("   ");
        for(int x = 1;x < 11 ;x++)
            System.out.print(x +"  ");
        System.out.println("");
        for(int i= 0;i<10;i++){
            System.out.print(String.format("%2d",i+1));
            for (int j=0;j<10;j++){
                if(map[j][i] == 0){
                    System.out.print("[ ]");
                }else{
                    System.out.print("["+map[j][i]+"]");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }
    static void printSearchMap(){//敵の探索用のマップ表示
        for(int i= 0;i<10;i++){
            for (int j=0;j<10;j++){
                System.out.print(String.format("%2d ",searchMap[j][i]));
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
    static void countClear(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                searchMap[i][j]=0;
            }
        }
    }
    static void choose (){
        int max = 0;
        int index = 0;//maxの値をもつ座標の個数
        int posx[] = new int [100];//配列は通し番号,maxの値を持つ座標を代入
        int posy[] = new int [100];
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                if (max==searchMap[j][i]){//maxとおなじなら
                    posx[index] = j;
                    posy[index] = i;
                    index++;
                } else if (max<searchMap[j][i]){//maxより小さいなら塗り替える
                    index = 1;
                    posx[0] = j;
                    posy[0] = i;
                    max = searchMap[j][i];
                }
            }
        }
        System.out.println("候補一覧");
        for(int i=0;i<index;i++){
            System.out.println("x,y : "+posx[i]+","+posy[i]);//候補一覧\nアイデア募集
        }//そもそも候補が複数になることって最初以外あるの？
        System.out.println("BON!!");
        System.out.println(""+posx[0]+","+posy[0]);//とりあえず最初の
    }
}
