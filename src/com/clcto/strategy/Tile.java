package com.clcto.strategy;

import android.graphics.*;
import java.util.HashSet;
import java.util.Random;

public class Tile 
{
      // static --------------------------
   private static Path drawPath;

   static
   {
      drawPath = new Path();
      drawPath.moveTo( 1, 0 );
      
      double d_rad = Math.PI / 3;
      
      for( double ang = 2 * Math.PI; ang > 0; ang -= d_rad )
         drawPath.lineTo( (float) Math.cos( ang ), 
                          (float) Math.sin( ang ) );

      drawPath.close();
   }

      
   private final int row, col;
   private final float x, y;
   private Paint brush;

   private int fill_color = 0;
   private GameMap map;
   private int mostMovePoints;
   private int moveCost = 1;

      // constructor using row and col index
   public Tile( GameMap m, int r, int c )
   {
      map = m;
      row = r;
      col = c;

      brush = new Paint();
      brush.setDither( true );
      brush.setAntiAlias( true );
      brush.setStrokeJoin( Paint.Join.ROUND );
      brush.setStrokeCap( Paint.Cap.ROUND );

      if( col % 2 == 0 )
      {
         x = (float) (col * 3.0f / 2.0 + 1);
         y = (float) ((row + 1) * Math.sqrt( 3 ));
      }
      else
      {
         x = (float) (2.5 + 3 * (col - 1) / 2.0);
         y = (float) (( 0.5 + row ) * Math.sqrt( 3 ));
      }
      
      mostMovePoints = -1;

      Random rand = new Random();
      moveCost = rand.nextInt( 3 ) + 1;
   }

   public void draw( Canvas canvas )
   {
      canvas.save();
      canvas.translate( (float) x, (float) y );
   
      brush.setColor( 0xFF000000 );
      brush.setStyle( Paint.Style.STROKE );

      canvas.drawPath( drawPath, brush );

      brush.setColor( fill_color );
      brush.setStyle( Paint.Style.FILL );

      canvas.drawPath( drawPath, brush );

      canvas.restore();
   }

   public double distance( float map_x, float map_y )
   {
      float[] point = { map_x - x, map_y - y };

      return PointF.length( point[0], point[1] );
   }

   public int getRow()
   {
      return row;
   }

   public int getCol()
   {
      return col;
   }

      // color is in the form that Brush.setColor() uses:
      //  > first byte is   opacity
      //  > second byte is  red
      //  > third byte is   green
      //  > fourth byte is  blue
   public void setFill( int color )
   {
      fill_color = color;
   }

   public HashSet<Tile> getPossibleMoves( int movePts )
   {
      HashSet<Tile> set = new HashSet<Tile>();
         
         // we add moveCost because it costs points to 
         // move into a new tile. since we are already in
         // this tile, we do not want to subtract its cost
         // which getPossibleMoves( HashSet<Tile>, int ) does
      getPossibleMoves( set, movePts + moveCost );

      for( Tile t : set )
         t.clearMovePoints();

      return set;
   }

   private void getPossibleMoves( HashSet<Tile> set, int ptsBefore )
   {
      int ptsAfter = ptsBefore - moveCost;

      if( ptsAfter < 0 ) return; // unable to enter
      if( ptsAfter <= mostMovePoints ) return;

      System.err.println( row + ", " + col );
      mostMovePoints = ptsAfter;
      set.add( this );

      for( Tile t : map.getSurrounding( row, col ) )
      {
         if( t == null )
            continue;

         t.getPossibleMoves( set, ptsAfter );         
      }
   }

   public int hashCode()
   {
      return row * 7 + col * 2;
   }

   private void clearMovePoints()
   {
      mostMovePoints = -1;
   }

   public boolean equals( Tile t )
   {
      return row == t.row && col == t.col;
   }
   
}
