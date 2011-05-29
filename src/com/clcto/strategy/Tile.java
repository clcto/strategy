package com.clcto.strategy;

import android.graphics.*;

public class Tile
{
      // static --------------------------
   public static int radius = 20;

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
   private final double x, y;
   private Paint brush;

   private Matrix transformation = new Matrix();

      // constructor using row and col index
   public Tile( int r, int c )
   {
      row = r;
      col = c;

      brush = new Paint();
      brush.setDither( true );
      brush.setAntiAlias( true );
      brush.setStrokeJoin( Paint.Join.ROUND );
      brush.setStrokeCap( Paint.Cap.ROUND );

      if( col % 2 == 0 )
      {
         x = col * 3.0 / 2.0 + 1;
         y = (row + 1) * Math.sqrt( 3 );
      }
      else
      {
         x = 2.5 + 3 * (col - 1) / 2.0;
         y = ( 0.5 + row ) * Math.sqrt( 3 );
      }
   }

   public void gameDraw( Canvas canvas )
   {
      canvas.save();
      canvas.scale( radius, radius );
      canvas.translate( (float) x, (float) y );
   
      brush.setColor( 0xFF000000 );
      brush.setStyle( Paint.Style.STROKE );

      canvas.drawPath( drawPath, brush );

      Matrix temp = new Matrix();
      canvas.getMatrix( temp );
      temp.invert( transformation );

      canvas.restore();
   }

   public double distance( float screen_x, float screen_y )
   {
      float[] point = { screen_x, screen_y };
      transformation.mapPoints( point );

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
}
