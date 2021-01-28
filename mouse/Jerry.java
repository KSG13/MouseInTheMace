/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouserun.mouse;

import java.util.HashMap;
import java.util.Stack;
import javafx.util.Pair;
import mouserun.game.Cheese;
import mouserun.game.Grid;
import mouserun.game.Mouse;

/**
 *
 * @author admin
 */
public class Jerry extends Mouse{

    private Grid ultima_casilla; //Última celda visitada.
    private final HashMap<Pair<Integer, Integer>, Grid> celdasVisitadas; //Hash de visitadas.
    private final Stack<Integer> pilaMovimientos; //Pila para recorrido.
    
    public Jerry() {
        super("Jerry");
        celdasVisitadas = new HashMap<>(); //Almacena las casillas que se van visitando.
        pilaMovimientos = new Stack<>(); //Almacena los movimientos del ratón para poder volver sobre nuestros pasos.
    } 
    
    //Función para ver si estamos en un callejón(al final no la usamos).
    public boolean Callejon(Grid currentGrid, int movimiento)
    {
        Pair siguiente = Traductor_pair(currentGrid,movimiento);
        
        
        if( !currentGrid.canGoUp() && !currentGrid.canGoLeft() && !currentGrid.canGoRight())//Callejon hacia arriba
        {
            return true;
        }
        if( !currentGrid.canGoLeft() && !currentGrid.canGoUp() && !currentGrid.canGoDown())//Callejon hacia izquierda
        {
            return true;
        }
        if( !currentGrid.canGoDown() && !currentGrid.canGoLeft() && !currentGrid.canGoRight())//Callejon hacia abajo
        {
            return true;
        }
        if( !currentGrid.canGoUp() && !currentGrid.canGoDown() && !currentGrid.canGoRight())//Callejon hacia derecha
        {
            return true;
        }
        
        return false;
    }
    
    //Función que nos devulve la casilla a la que nos vamos a desplazar, dada la casilla en la que estamos y un movimiento.
    public Grid Traductor_grid(Grid currentGrid,int n)
    {
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        
        if(n==Mouse.UP)
        {
            Grid siguiente = new Grid(x,y+1);
            return siguiente;
        }
        if(n==Mouse.RIGHT)
        {
            Grid siguiente = new Grid(x+1,y);
            return siguiente;
        }
        if(n==Mouse.DOWN)
        {
            Grid siguiente = new Grid(x,y-1);
            return siguiente;
        }
        if(n==Mouse.LEFT)
        {
            Grid siguiente = new Grid(x-1,y);
            return siguiente;
        }
        return currentGrid;
    }
    
    
    public Pair Traductor_pair(Grid currentGrid,int n)
    {
        int x = currentGrid.getX();
        int y = currentGrid.getY();
        
        if(n==Mouse.UP)
        {
            Pair siguiente = new Pair(x,y+1);
            return siguiente;
        }
        if(n==Mouse.RIGHT)
        {
            Pair siguiente = new Pair(x+1,y);
            return siguiente;
        }
        if(n==Mouse.DOWN)
        {
            Pair siguiente = new Pair(x,y-1);
            return siguiente;
        }
        if(n==Mouse.LEFT)
        {
            Pair siguiente = new Pair(x-1,y);
            return siguiente;
        }
        
        Pair siguiente = new Pair(x,y);
        return siguiente;
    }
    
    
    @Override
    public int move(Grid currentGrid, Cheese cheese) {

        int x = currentGrid.getX();
        int y = currentGrid.getY();
        
        Pair< Integer , Integer > p =new Pair<>(x,y);
        
        //Función para cuando se haya recorrido todo el laberinto y se vuelva al inicio recogiendo pila.
        if( pilaMovimientos.empty() && !celdasVisitadas.isEmpty())
        {
            if(currentGrid.canGoUp())
            {
                return Mouse.UP;
            }
            if(currentGrid.canGoDown())
            {
                return Mouse.DOWN;
            }
            if(currentGrid.canGoRight())
            {
                return Mouse.RIGHT;
            }
            if(currentGrid.canGoLeft())
            {
                return Mouse.LEFT;
            }
            return Mouse.BOMB;
        }
        
        //Añadimos la celda en la que nos encontramos a la tabla hash.
        celdasVisitadas.put(p, currentGrid);
        
        //El ratón va hacia arriba, siempre y cuando se pueda y la casilla no haya sido visitada.
        Pair siguiente = Traductor_pair(currentGrid,Mouse.UP);
        if(currentGrid.canGoUp() && !celdasVisitadas.containsKey(siguiente))
        {
            pilaMovimientos.add(Mouse.UP);
            incExploredGrids();
            return Mouse.UP;
        }
        //El ratón va hacia abajo, siempre y cuando se pueda y la casilla no haya sido visitada.
        siguiente = Traductor_pair(currentGrid,Mouse.DOWN);
        if(currentGrid.canGoDown()&& !celdasVisitadas.containsKey(siguiente))
        {
            pilaMovimientos.add(Mouse.DOWN);
            incExploredGrids();
            return Mouse.DOWN;
        }
        //El ratón va hacia la derecha, siempre y cuando se pueda y la casilla no haya sido visitada.
        siguiente = Traductor_pair(currentGrid,Mouse.RIGHT);
        if(currentGrid.canGoRight()&& !celdasVisitadas.containsKey(siguiente))
        {
            pilaMovimientos.add(Mouse.RIGHT);
            incExploredGrids();
            return Mouse.RIGHT;
        }
        //El ratón va hacia la izquierda, siempre y cuando se pueda y la casilla no haya sido visitada.
        siguiente = Traductor_pair(currentGrid,Mouse.LEFT);
        if(currentGrid.canGoLeft()&& !celdasVisitadas.containsKey(siguiente))
        {
            pilaMovimientos.add(Mouse.LEFT);
            incExploredGrids();
            return Mouse.LEFT;
        }
        
        //Volver sobre nuestros pasos almecenados en la pila una vez que no podamos avanzar.
        if( pilaMovimientos.lastElement() == Mouse.UP)
        {
            pilaMovimientos.pop();
            return Mouse.DOWN;
        }
        if( pilaMovimientos.lastElement() == Mouse.DOWN)
        {
            pilaMovimientos.pop();
            return Mouse.UP;
        }
        if( pilaMovimientos.lastElement() == Mouse.LEFT)
        {
            pilaMovimientos.pop();
            return Mouse.RIGHT;
        }
        if( pilaMovimientos.lastElement() == Mouse.RIGHT)
        {
            pilaMovimientos.pop();
            return Mouse.LEFT;
        }
        
        return Mouse.BOMB;      
    }

    @Override
    public void newCheese() {
        ///
    }

    @Override
    public void respawned() {
        pilaMovimientos.clear();
    }
}
    
