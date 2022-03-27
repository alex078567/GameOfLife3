public class algorithm {
    private int height;
    private int width;
    private int Asize;
    private int Seednumber;
    byte[] gen ;
    /*Реализация алгоритма qlife из книги
    https://www.jagregory.com/abrash-black-book/#chapter-17-the-game-of-life
    Главная идея заключается в том, что каждая клетка поля представляется
    переменной типа byte xxxx_xxxx
    где первый бит 0000_000x содержит информацию о состоянии клетки
    а следующие четыре информацию о соседях 000xxxx0
     */
    public algorithm(int width, int height) {
        //здесь задаются ширина и высота поля
        //(в нашем случае они одинаковы)
        this.width = width;
        this.height = height;
        this.Asize = width * height;
        gen = new byte[Asize];
    }
    //Оживить клетку и передать всем соседям
    //информацию о том, что она жива
    public void setCell(int x,int y){
        int xleft,xright,yup,ydown;
        int pos= x + (y * width);
        //поле непрерывно(тоесть координата конца соединяется с началом)
        if (x == 0)
            xleft = width - 1;
        else
            xleft = -1;
        if (y == 0)
            yup = Asize - width;
        else
            yup = -width;
        if (x == (width - 1))
            xright = -(width - 1);
        else
            xright = 1;
        if (y == (height - 1))
            ydown = -(Asize - width);
        else
            ydown = width;
        //передаем всем соседям информацию что появилась живая клетка рядом
        gen[pos] |= 0x01;
        gen[pos+yup+xleft] +=2;
        gen[pos+yup+xright] +=2;
        gen[pos+yup] +=2;
        gen[pos+xleft] +=2;
        gen[pos+xright] +=2;
        gen[pos+ydown+xleft] +=2;
        gen[pos+ydown+xright] +=2;
        gen[pos+ydown] +=2;
    }
    //уничтожить клетку
    public void clearCell(int x,int y){
        int xleft,xright,yup,ydown;
        int pos= x + (y * width);

        if (x == 0)
            xleft = width - 1;
        else
            xleft = -1;
        if (y == 0)
            yup = Asize - width;
        else
            yup = -width;
        if (x == (width - 1))
            xright = -(width - 1);
        else
            xright = 1;
        if (y == (height - 1))
            ydown = -(Asize - width);
        else
            ydown = width;
        //уничтожить клетку и передать всем соседям информацию о том что
        //ее больше нет рядом
        gen[pos] &= ~0x01;
        gen[pos+yup+xleft] -=2;
        gen[pos+yup+xright] -=2;
        gen[pos+yup] -=2;
        gen[pos+xleft] -=2;
        gen[pos+xright] -=2;
        gen[pos+ydown+xleft] -=2;
        gen[pos+ydown+xright] -=2;
        gen[pos+ydown] -=2;
        gen[pos+ydown]=gen[pos+ydown];
    }
    //проверяем состояние клетки (жива или нет)
    public int cellState(int x,int y){
        return gen[x+y*width]&0x01;
    }
    //метод который создает новое поколение
    public void nextgen() {
        //массив в котором содержиться старое поколение

        byte[] lostgen = new byte[Asize] ;
        int count,x,y;
        System.arraycopy(gen, 0, lostgen, 0, Asize);

        for ( y = 0; y < height; y++) {
         x = 0;
        row:
            do{
                //здесь пропускаются все клетки которые не живы и не содержат живых соседей
                while (lostgen[x + (y * width)]==0){
                    x++;
                    //если вдруг x выходит за пределы строки, то прервать цикл который помечен
                    //меткой row
                    if(x>=width){
                        break row;
                    }
                }

            count = lostgen[x + (y * width)]>>1;
                //если клетка жива
            if((lostgen[x + (y * width)]&0x01)==1){
                //если количество соседей не два и не три
                if((count!=2)&&(count!=3)){
                    //то уничтожаем клетку
                    clearCell(x, y);
                }
            }
            else{
                //если количество соседей равно трем, то оживляем клетку
                if (count==3) {
                    setCell(x, y);
                }
            }
            x++;
        }while(x<width);

        }
       // System.arraycopy(gen, 0, lostgen, 0, Asize);
    }

       //метод который засеевает поле жизнью
    public void RandomSeed(int seeds) {
        Seednumber = seeds;
        for (int i = 0; i < Seednumber; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            //здесь очень важно проверять не засеяна ли уже клетка
            //так как если ее засеять повторно то будет
            //передана неверная информация всем ее соседям
            if (cellState(x, y) == 0) {
                setCell(x, y);
            }
        }
    }
    // метод который возвращает массив gen где содержится состояние поля на текущий момент
    public byte[] getGen() {
        return gen;
    }
}
