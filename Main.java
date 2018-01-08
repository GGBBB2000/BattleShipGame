import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;
class Main {
    static int order = 1;//先行なら0
    static int map[][] = new int [10][10];//自分のマップ
    static int enemyMap[][] = new int [10][10];//敵のマップ
    static int searchMap[][] = new int [10][10];//敵を探索するときのマップ
    static int debugMap[][] = new int [10][10];//debugマップ
    static int mode = 0;
    static int dir = 0;//0なら探索中,1なら横,2なら縦
    static int maxLength = 5;
    static int minLength = 2;
    static int shootx = 0;
    static int shooty = 0;
    static Random rnd = new Random();
    static int right= 0;
    static int up= 0;
    static int left= 0;
    static int down= 0;
    static int totalHorizontal = 0;
    static int totalVertical = 0;
    Fleet CV = new Fleet(2,2,0,5);//船の場所を自動生成するメソッドを作るまではこれで行く
    Fleet BattleShip = new Fleet(4,4,0,4);//もしかしたらこんなのいらないかもしれない
    Fleet Cruiser1 = new Fleet(5,5,1,3);
    Fleet Cruiser2 = new Fleet(6,6,0,3);
    Fleet Destroyer = new Fleet(7,7,0,2);
    static int hp [] = {5,2,3,3,4,5};//船のIDに合わせて。index0番めは残りの船の数
    static int enemyHp[] = {5,1,2,1,1};
    static int deff= 0;
    public static void main (String args[]){
        initialize();
        if (order==1){
            do{
                deffense();
            }while(deff==1);
        }
        while(true){
            do{
                if (!attack()){
                    return;
                }
            }while(mode==1);//mode 0 :あたってない mode 1:撃沈してもう一度探索 mode 2:方向探索中
            do{
                if (!deffense()){
                    return;
                }
            }while(deff==1);
        }
    }

    static void initialize(){
        enemyClear();
        put(2,2,0,5,5);//空母のIDは5
        put(4,4,0,4,4);//戦艦のIDは4
        put(5,5,1,3,3);//巡洋艦1のIDは3
        put(6,6,0,3,2);//巡洋艦2のIDは2
        put(7,7,0,2,1);//駆逐艦のIDは1
        printMap();
    }
    static boolean attack(){
        if (mode==1){
            mode = 0;
        }
        boolean boo = true;
        if (mode==0){
            //一回目の攻撃
            //countClear()
	    for(int i=0;i<10;i++){
		    for(int j=0;j<10;j++){
			searchMap[i][j]=0;
		    }
	    }
            count(2);//ここなんかダサい
            count(3);
            count(3);
            count(4);
            count(5);
            printSearchMap();
            if(!choose()){
                return true;
            }
            mode = 2;
            right = 0;
            left = 0;
            up = 0;
            down = 0;
            totalHorizontal = 0;
            totalVertical = 0;

        }

        while(hunt(shootx,shooty)){
            System.out.println("huntcalled");
        }
        return boo;
    }
    static boolean deffense(){
        deff = 0;
        boolean boo = true;
        System.out.println("敵の攻撃\nスペース区切りで座標を入力\n左上は1,1");
        int x = 0;
        int y = 0;
        boolean b = true;
        do{
            Scanner sc = new Scanner(System.in);
            try{
                x = sc.nextInt()-1;
                y = sc.nextInt()-1;
                b = true;
            }catch (InputMismatchException e) {
                b = false;
                System.out.println("異なる値が代入されました");
            }

        }while(!b);
        if (map[x][y]>0){
            int i = map[x][y];
            map[x][y]=0;
            printMap();
            //map[x][y]*=-1;//こっちやったら書式崩れたので
            hp[i]--;
            if (hp[i]==0){
                System.out.println("撃沈");
                hp[0]--;
                if (hp[0]==0){
                    System.out.println("負け");
                    boo = false;
                }
                deff = 1;
            }else {
                System.out.println("Hit!!");
                deffense();
            }
        }else{
            System.out.println("ハズレ");
        }
        return boo;
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
        System.out.println("printMap"+"\u001b[34m");
        System.out.print("   ");
        for(int x = 1;x < 11 ;x++)
            System.out.print(x +"   ");
        System.out.println("");
        for(int i= 0;i<10;i++){
            System.out.print(String.format("%2d",i+1));
            for (int j=0;j<10;j++){
                if(map[j][i] == 0){
                    System.out.print("[  ]");
                }else{
                    System.out.print("["+String.format("%2d",map[j][i])+"]");
                }
            }
            System.out.println("");
        }
        System.out.println("\u001b[0m");
    }
    static void printDebug(){//debug表示
        System.out.println("printDebug"+"\u001b[32m");
        System.out.print("   ");
        for(int x = 1;x < 11 ;x++)
            System.out.print(x +"   ");
        System.out.println("");
        for(int i= 0;i<10;i++){
            System.out.print(String.format("%2d",i+1));
            for (int j=0;j<10;j++){
                if(map[j][i] == 0){
                    System.out.print("[  ]");
                }else{
                    System.out.print("["+String.format("%2d",map[j][i])+"]");
                }
            }
            System.out.println("");
        }
        System.out.println("\u001b[0m");
    }
    static void printEnemyMap(){//tekiのマップ表示
        System.out.println("printEnemyMap"+"\u001b[33m");
        System.out.print("   ");
        for(int x = 1;x < 11 ;x++)
            System.out.print(x +"   ");
        System.out.println("");
        for(int i= 0;i<10;i++){
            System.out.print(String.format("%2d",i+1));
            for (int j=0;j<10;j++){
                if(enemyMap[j][i] == 0){
                    System.out.print("[  ]");
                }else{
                    System.out.print("["+String.format("%2d",enemyMap[j][i])+"]");
                }
            }
            System.out.println("");
        }
        System.out.println("\u001b[0m");
    }
    static void printSearchMap(){//敵の探索用のマップ表示
        System.out.println("printSearchMap"+"\u001b[31m");
        for(int i= 0;i<10;i++){
            for (int j=0;j<10;j++){
                System.out.print(String.format("%2d ",searchMap[j][i]));
            }
            System.out.println("");
        }
        System.out.println("\u001b[0m");
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
    static boolean hunt(int x,int y){
        if(dir == 0){
            for(int i=1;i-1<maxLength+1;i++){
                if(x-i>=0&&enemyMap[x-i][y]==0){
                    //System.out.println("left "+(x-i+1)+","+(y+1));
                    left++;
                }else if (x-i>=0&&enemyMap[x-i][y]!=0){
                    break;
                }
            }
            for(int i=1;i<maxLength+1;i++){
                if(x+i<=9&&enemyMap[x+i][y]==0){
                    //System.out.println("right "+(x+1+i)+","+(y+1));
                    right++;
                }else if (x+i<=9&&enemyMap[x+i][y]!=0){
                    break;
                }
            }
            for(int i=1;i<maxLength+1;i++){
                if(y-i>=0&&enemyMap[x][y-i]==0){
                    //System.out.println("up "+(x+1)+","+(y-i+1));
                    up++;
                }else if (y-i>=0&&enemyMap[x][y-i]!=0){
                    break;
                }
            }
            for(int i=1;i<maxLength+1;i++){
                if(y+i<=9&&enemyMap[x][y+i]==0){
                    //System.out.println("down "+(x+1)+","+(y+i+1));
                    down++;
                }else if (y+i<=9&&enemyMap[x][y+i]!=0){
                    break;
                }
            }
            totalHorizontal = right+left;
            totalVertical = up+down;
            System.out.println("H,V"+totalHorizontal+","+totalVertical);
            if (totalHorizontal<=totalVertical){
                if (up>=down){
                    shootx = x;
                    shooty = y-1;
                    up--;
                    dir = 1;
                }else{
                    shootx = x;
                    shooty = y+1;
                    down--;
                    dir = 1;
                }
            }else{
                if (left>=right){
                    shootx = x-1;
                    shooty = y;
                    left--;
                    dir = 2;
                }else{
                    shootx = x+1;
                    shooty = y;
                    right--;
                    dir = 2;
                }
            }
            System.out.println("shoot x,y :"+(shootx+1)+","+(shooty+1));
            if(check()){
                if (gktncheck()){
                    enemyMap[shootx][shooty]=-3;
                    System.out.println("撃沈");
                    dir = 0;
                    printEnemyMap();
                    return false;

                }else{
                    enemyMap[shootx][shooty]=-1;
                    System.out.println("Hit!!");
                    printEnemyMap();
                    return true;
                }
            }else{
                enemyMap[shootx][shooty]=-2;
                System.out.println("はずれ");
                dir = 0;
                printEnemyMap();
                return false;
            }
        }else if(dir==1){
            System.out.println("dir1");
            if (up<=down){
                down--;
                for (int i=1;i<10;i++){
                    if(y+i<=9&&enemyMap[x][y+i]==0){
                        shootx = x;
                        shooty = y+i;
                        break;
                    }else if(y+i<=9&&enemyMap[x][y+i]!=-1){
                        break;
                    }
                }
            }else{
                up--;
                for (int i=1;i<10;i++){
                    if(y-i>=0&&enemyMap[x][y-i]==0){
                        shootx= x;
                        shooty= y-i;
                        break;
                    }else if(y-i>=0&&enemyMap[x][y-i]!=-1){
                        break;
                    }
                } 
            }
            System.out.println("shoot x,y : "+(shootx+1)+","+(shooty+1));
            if(check()){
                if (gktncheck()){
                    enemyMap[shootx][shooty]=-3;
                    System.out.println("撃沈");
                    dir = 0;
                    printEnemyMap();
                    return false;
                }else{
                    enemyMap[shootx][shooty]=-1;
                    System.out.println("Hit!!");
                    printEnemyMap();
                    return true;
                }
            }else{
                System.out.println("はずれ");
                enemyMap[shootx][shooty]=-2;//外れたら-2を書き込み
                printEnemyMap();
                return false;
            }
        
        }else{
            System.out.println("dir2");
            if (right>=left){
                right--;
                for (int i=1;i<10;i++){
                    if(x+i<=9&&enemyMap[x+i][y]==0){
                        shootx = x+i;
                        shooty = y;
                        break;
                    }else if(x+i<=9&&enemyMap[x+i][y]!=-1){
                        break;
                    }
                }
            }else{
                left--;
                for (int i=1;i<10;i++){
                    if(x-i>=0&&enemyMap[x-i][y]==0){
                        shootx= x-i;
                        shooty= y;
                        break;
                    }else if(x-i>=0&&enemyMap[x-i][y]!=-1){
                        break;
                    }
                } 
            }
            System.out.println("shoot x,y : "+(shootx+1)+","+(shooty+1));
            if(check()){
                if (gktncheck()){
                    enemyMap[shootx][shooty]=-3;
                    System.out.println("撃沈");
                    dir = 0;
                    printEnemyMap();
                    return false;
                }else{
                    enemyMap[shootx][shooty]=-1;
                    System.out.println("Hit!!");
                    printEnemyMap();
                    return true;
                }
            }else{
                System.out.println("はずれ");
                enemyMap[shootx][shooty]=-2;//外れたら-2を書き込み
                printEnemyMap();
                return false;
            }
        }
    }

    static void countClear(){
    }
    static void enemyClear(){
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                enemyMap[i][j]=0;
            }
        }
    }
    static boolean choose (){
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
            System.out.println("x,y : "+(posx[i]+1)+","+(posy[i]+1));//候補一覧\nアイデア募集
        }//そもそも候補が複数になることって最初以外あるの？
        shootx = posx[0];
        shooty = posy[0];
        System.out.println("shootx,y :"+(posx[0]+1)+","+(posy[0]+1));//とりあえず最初の
        if(check()){
            if (gktncheck()){
                enemyMap[shootx][shooty]=-3;
                System.out.println("撃沈");
                dir = 0;
                printEnemyMap();
                return false;

            }else{
                enemyMap[shootx][shooty]=-1;
                System.out.println("Hit!!");
                printEnemyMap();
                return true;
            }
        }else{
            System.out.println("はずれ");
            enemyMap[shootx][shooty]=-2;//外れたら-2を書き込み
            printEnemyMap();
            return false;
        }
    }
    static boolean check(){
        System.out.println("あたったら0,外れたらその他整数");
        int s = 0;
        boolean b = true;
        do{
            Scanner sc = new Scanner(System.in);
            try{
                s = sc.nextInt();
                b = true;
            }catch (InputMismatchException e) {
                b = false;
                System.out.println("異なる値が代入されました");
            }
        }while(!b);
        if (s==0){
            return true;
        }else{
            return false;
        }
    }
    static boolean gktncheck(){
        System.out.println("撃沈なら0,そうでないならその他整数");
        int s = 0;
        boolean b = true;
        do{
            Scanner sc = new Scanner(System.in);
            try{
                s = sc.nextInt();
                b = true;
            }catch (InputMismatchException e) {
                b = false;
                System.out.println("異なる値が代入されました");
            }
        
        }while(!b);
        
        if (s==0){
            int length=0;
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    if (enemyMap[i][j]==-1){
                        length++;
                        enemyMap[i][j]=-3;
                    }
                }
            }
            if (length+1>=0&&enemyHp[length+1]>=1){
                enemyHp[length+1]--;
                System.out.println("長さ"+(length+1)+"を撃沈?");
            }
            mode=1;
            return true;
        }else{
            return false;
        }
    }
}

