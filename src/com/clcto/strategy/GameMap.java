package com.clcto.strategy;

import java.util.*;
import android.graphics.*;
import android.content.*;
import android.view.*;

public class GameMap extends View
{
   private ArrayList<ArrayList<Tile>> board;
   private GameMap.TouchListener touch_listener;
   private GameMap.ScaleListener scale_listener;
   private GestureDetector gestures;
   private ScaleGestureDetector scale_gestures;

      // the xy map coordinate that is the top left of the screen
   private PointF translation = new PointF( 0, 0 );
   private float scale = 1;

   private Matrix transformation = new Matrix();
   
   private ArrayList<Tile> possible_tiles = new ArrayList<Tile>();

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

      for( ArrayList<Tile> list : board )
         for( Tile t : list )
            t.gameDraw( canvas );

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
      possible_tiles.clear();

      Tile best = null;
      double min_dist = Double.MAX_VALUE;

      for( ArrayList<Tile> list : board )
         for( Tile t : list )
         {
            double dist = t.distance( s_x, s_y );

            if( dist < min_dist )
            {
               min_dist = dist;
               best = t;
            }
         }

      return best;
   }

   private class TouchListener extends 
                        GestureDetector.SimpleOnGestureListener
   {
      @Override
      public boolean onSingleTapConfirmed( MotionEvent e )
      {
         Tile t = getTile( e.getX(), e.getY() );
         if( t != null )
            System.err.println( t.getRow() + " " + t.getCol() );
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
         scale = Math.max( 0.2f, Math.min( scale, 2f ) );

         return true;
      }
   }

}

