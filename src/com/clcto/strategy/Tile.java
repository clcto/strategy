package com.clcto.strategy;

import android.graphics.*;

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
         x = (float) (col * 3.0f / 2.0 + 1);
         y = (float) ((row + 1) * Math.sqrt( 3 ));
      }
      else
      {
         x = (float) (2.5 + 3 * (col - 1) / 2.0);
         y = (float) (( 0.5 + row ) * Math.sqrt( 3 ));
      }

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
   
}
