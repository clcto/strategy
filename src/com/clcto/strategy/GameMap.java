package com.clcto.strategy;

import java.util.*;
import android.graphics.*;
import android.content.*;
import android.view.*;

public class GameMap extends View
{
   private ArrayList<ArrayList<Tile>> board;

   public GameMap( Context context, int num_row, int num_col )
   {
      super( context );
      board = new ArrayList< ArrayList<Tile> >();

      for( int row = 0; row < num_row; ++row )
      {
         ArrayList<Tile> row_list = new ArrayList<Tile>();
         board.add( row_list );
         for( int col = 0; col < num_col; ++col )
            row_list.add( new Tile( row, col ) ); 
      }
   }

   @Override
   public void onDraw( Canvas canvas )
   {
      for( ArrayList<Tile> list : board )
         for( Tile t : list )
            t.gameDraw( canvas );
   }

   @Override
   public void onMeasure( int w_ms, int h_ms )
   {
      int w, h;
      w = View.MeasureSpec.getSize( w_ms );
      h = View.MeasureSpec.getSize( h_ms );
      h = (int) (h * 2.0/3.0);

      setMeasuredDimension( w, h );
   }

}

