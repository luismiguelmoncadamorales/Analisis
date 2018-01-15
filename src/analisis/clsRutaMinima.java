/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import java.awt.Point;
import java.util.ArrayList;


public class clsRutaMinima {
  public int deposito=0;
     
    public String  adyascentes(int i, int j,int iposicion,int jposicion){
        String salida="";
        if (((iposicion-1)==i)&&(j==jposicion)) {
            salida="arriba";
        }
        else  if((iposicion==i)&&(j==(jposicion-1))){
             salida="izquierda";
        }
         else  if ((iposicion==i)&&(j==(jposicion+1))) {
            salida ="derecha";
        }
         else if (((iposicion+1)==i)&&(j==jposicion)) {
            salida ="abajo";
        }
         else{
            salida ="no"; 
         }
        return salida;
    }
     
public ArrayList<Point> camino(int[][]matriz,int i,int j,int iorigen,int jorigen,int distancia,ArrayList<Point>p){
    int k=0;
    int l=0;
    Point t=new Point(i, j);
    p.add(t);
   // System.out.println("posicion"+i+","+j);
    while(k<8){
        l=0;
        while(l<12){
            if ((adyascentes(k, l, i, j).equalsIgnoreCase("arriba"))&& ((matriz[k][l]==-1)||(matriz[k][l]==deposito))) {
                if ((k!=iorigen) || (l!=jorigen)) {
                    if (((solucion(matriz ,k, l,i,j))+1)<=distancia) {
                        camino(matriz ,k, l,i,j,distancia-1,p);
                    }
                }
            }
            if ((adyascentes(k, l, i, j).equalsIgnoreCase("izquierda"))&& ((matriz[k][l]==-1)||(matriz[k][l]==deposito))) {
                if ((k!=iorigen) || (l!=jorigen)) {
                    if (((solucion(matriz ,k, l,i,j))+1)<=distancia) {
                        camino(matriz ,k, l,i,j,distancia-1,p);
                    }
                }
            }
            if ((adyascentes(k, l, i, j).equalsIgnoreCase("derecha"))&& ((matriz[k][l]==-1)||(matriz[k][l]==deposito))) {
                if ((k!=iorigen) || (l!=jorigen)) {
                    if (((solucion(matriz ,k, l,i,j))+1)<=distancia) {
                        camino(matriz ,k, l,i,j,distancia-1,p);
                    }
                }
            }
            if ((adyascentes(k, l, i, j).equalsIgnoreCase("abajo"))&& ((matriz[k][l]==-1)||(matriz[k][l]==deposito))) {
                if ((k!=iorigen) || (l!=jorigen)) {
                    if (((solucion(matriz ,k, l,i,j))+1)<=distancia) {
                        camino(matriz ,k, l,i,j,distancia-1,p);
                    }
                }
            }
            l++;
        }
        k++;
    }
    return p;
}
     public boolean sinsalida(int matriz[][],int i,int j){
         boolean salida=true;
         int k=0;//8
         int l=0;//12
         while(k<8){
             l=0;
             while(l<12){
                 if ((!adyascentes(k, l, i, j).equals("no"))&&(matriz[k][l]==-1)) {
                     salida=false;
                 }
                 l++;
             }
             k++;
         }
         return salida;
     }
   public boolean llegue(int matriz[][], int i, int j){
int k = 0; //8
int l = 0; //12
boolean salida = false;
    while (k<8){
      l=0;
        while (l<12){                   
          if ((!adyascentes(k, l, i, j).equals("no")) && (matriz[k][l]==deposito)){
          salida=true;
          }                          
            l=l+1;
        }
            k=k+1;
      }                                           
   return salida;  
}

public int solucion(int matriz[][], int i, int j, int iorigen, int jorigen){

int [] valores = new int[4];
int minimo;
int k = 0;
int l = 0;
int iter=0;

if (sinsalida(matriz,i,j)){
    return 99;
}else if ((llegue(matriz,i, j))){
    
    return 1;
}else{
for (int m = 0; m < 4; m++) {
    valores[m]=99;
    }
}
while (k<8){
  l=0;
  while(l<12){
       if ((adyascentes(k, l, i, j).equals("arriba"))&& (matriz[k][l]==-1) ){
        if ((k!=iorigen) || (l!=jorigen)){
         valores[0] = solucion(matriz ,k, l,i,j)+1;
        }
        }
        if ((adyascentes(k, l, i, j).equals("izquierda"))&& (matriz[k][l]==-1)){
                if ((k!=iorigen) || (l!=jorigen)) {
                  valores[1] = solucion(matriz ,k, l,i,j)+1;
                }
        }
        if ((adyascentes(k, l, i, j).equals("derecha"))&& (matriz[k][l]==-1)){                                                              
        if ((k!=iorigen) || (l!=jorigen)){
        valores[2] =solucion(matriz ,k, l,i,j)+1;
        }
        }
        if ((adyascentes(k, l, i, j).equals("abajo"))&& (matriz[k][l]==-1)){
        if ((k!=iorigen) || (l!=jorigen)){
        valores[3] = solucion(matriz ,k, l,i,j)+1;
        }
        }
        l=l+1;
  }
  k=k+1;
}     
        minimo = valores[0];
        iter =0;
        while (iter < valores.length){                   
            if (minimo > valores[iter]) {
               minimo = valores[iter];                      
            }
               iter = iter+1;
        }
         return minimo;           
      } 
    
    
    
    
} 
    
    
    
