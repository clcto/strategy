package com.clcto.strategy;

import java.util.*;
import android.graphics.*;
import android.content.*;
import android.view.*;

public class GameMap extends View
{
   private Tile[][] board;
   private GameMap.TouchListener touch_listener;
   private GameMap.ScaleListener scale_listener;
   private GestureDetector gestures;
   private ScaleGestureDetector scale_gestures;

      // the xy map coordinate that is the top left of the screen
   private PointF translation = new PointF( 0, 0 );
   private float scale = 20;

   private Matrix transformation = new Matrix();
   
   private Tile selected;

   public GameMap( Context context, int num_row, int num_col )
   {
      super( context );
      board = new Tile[ num_row + 2 ][ num_col + 2 ];

         // since we have a row of 'nulls' on each edge
         // we use   ` <= '
      for( int row = 1; row <= num_row; ++row )
         for( int col = 1; col <= num_col; ++col )
            board[ row ][ col ] = new Tile( this, row, col );

      touch_listener = new GameMap.TouchListener();
      gestures = new GestureDetector( 
                              context, touch_listener );

      scale_listener = new GameMap.ScaleListener();
      scale_gestures = new ScaleGestureDetector( 
                              context, scale_listener );
   }

   @Override
   public void onDraw( Canvas canvas )
   {
      canvas.drawColor( 0xFFDDDDDD );
      canvas.translate( - translation.x, - translation.y );
      canvas.scale( scale, scale );

      for( Tile[] list : board )
         for( Tile t : list )
         {
            if( t == null )
               continue;
            t.draw( canvas );
         }

      canvas.getMatrix( transformation );
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

   @Override
   public boolean onTouchEvent( MotionEvent e )
   {
      scale_gestures.onTouchEvent( e );

      if( !scale_gestures.isInProgress() )
         gestures.onTouchEvent( e );

      invalidate();
      return true;
   }

   public Tile getTile( float s_x, float s_y )
   {
      Tile best = null;
      double min_dist = Double.MAX_VALUE;

      float m_x = (s_x + translation.x) / scale;
      float m_y = (s_y + translation.y) / scale;
      

      for( Tile[] list : board )
         for( Tile t : list )
         {
            if( t == null )
               continue;

            double dist = t.distance( m_x, m_y );

            if( dist < 1 && dist < min_dist )
            {
               min_dist = dist;
               best = t;
            }
         }

      return best;
   }

   public Tile[] getSurrounding( int row, int col )
   {
      Tile[] surrounding = new Tile[6]; // hexagons have 6 neighbors

      surrounding[0] = board[ row - 1 ][ col     ];
      surrounding[1] = board[ row + 1 ][ col     ];
      surrounding[2] = board[ row     ][ col - 1 ];
      surrounding[3] = board[ row     ][ col + 1 ];

      if( col % 2 == 0 )
      {
         surrounding[4] = board[ row + 1 ][ col - 1 ];
         surrounding[5] = board[ row + 1 ][ col + 1 ];
      }
      else
      {
         surrounding[4] = board[ row - 1 ][ col - 1 ];
         surrounding[5] = board[ row - 1 ][ col + 1 ];
      }

      return surrounding;
   }

   private class TouchListener extends 
                        GestureDetector.SimpleOnGestureListener
   {
      @Override
      public boolean onSingleTapConfirmed( MotionEvent e )
      {
         /*
         if( selected != null )
            selected.setFill( 0x00000000 );
         */
         
         selected = getTile( e.getX(), e.getY() );
         if( selected != null )
         {
            HashSet<Tile> moves = selected.getPossibleMoves( 3 );
            //Tile[] moves = getSurrounding( selected.getRow(), selected.getCol() );
            for( Tile t : moves )
               t.setFill( 0xFFCC1111 );
         }
         
         invalidate();
          
         return true;
      }

      @Override
      public boolean onDoubleTap( MotionEvent e )
      {
         System.err.println( "double tap" );
         return true;
      }

      @Override
      public boolean onScroll( MotionEvent e1, MotionEvent e2,
                               float dx, float dy )
      {
         translation.offset( dx, dy );
         return true;
      }
      
   }

   private class ScaleListener extends 
                        ScaleGestureDetector.SimpleOnScaleGestureListener
   {
      @Override
      public boolean onScale( ScaleGestureDetector sgd )
      {
         scale *= sgd.getScaleFactor();
         scale = Math.max( 5, Math.min( scale, 50 ) );

         return true;
      }
   }

}

