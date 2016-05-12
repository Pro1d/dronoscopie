package fr.insatoulouse.stereoshot;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class CardBoard {
	public static double scale = 0.50-0.076, shape = 0.25, deltaScale = 0, deltaShape = 0.0; 
	public static void drawWithDistortion(Bitmap image, Rect src, Rect dst, Canvas canvas) {
		int w = dst.width(), h = dst.height();
		int[] pixels = new int[src.width()*src.height()];
		image.getPixels(pixels, 0, src.width(), src.left, src.top, src.width(), src.height());
		int[] out = fisheye(pixels, w,h, src.width(),src.height());
		canvas.drawBitmap(out, 0, w, dst.left, dst.top, dst.width(), dst.height(), false, null);
	}
	public static int[] fisheye(int[] srcpixels, double w, double h, int w_src, int h_src) {

		/*
		 * Fish eye effect tejopa, 2012-04-29 http://popscan.blogspot.com
		 * http://www.eemeli.de
		 */
		double k = scale+deltaScale, l = shape+deltaShape;
		// create the result data
		int[] dstpixels = new int[(int) (w * h)];
		double h_2 = h/2, w_2 = w/2;
		int wInt = (int) w, hInt = (int) h;
		double w_src_2 = w_src * 0.5, h_src_2 = h_src * 0.5;
		// for each row
		double ny = -k, dny = 1.0 / h_2 * k;
		double nx = -k, dnx = 1.0 / w_2 * k;
		for (int y = 0; y < hInt; y++) {
			// normalize y coordinate to -1 ... 1
			ny += dny;
			// pre calculate ny*ny
			double ny2 = ny * ny;
			nx = -k;
			// for each column
			for (int x = 0; x < wInt; x++) {
				// normalize x coordinate to -1 ... 1
				nx += dnx;//(x-w_2)/(w_2)*k;//((2 * x) / w*k) - 1;
				// pre calculate nx*nx
				double nx2 = nx * nx;
				// calculate distance from center (0,0)
				// this will include circle or ellipse shape portion
				// of the image, depending on image dimensions
				// you can experiment with images with different dimensions
				double r2 = nx2 + ny2;
				// discard pixels outside from circle!
				if (r2 <= 1.0) {
					double r = Math.sqrt(r2);
					double nr = Math.sqrt(1.0 - r2);
					// new distance is between 0 ... 1
					nr = (r + (1.0 - nr)) * 0.5;
					// discard radius greater than 1.0
					if (nr <= 1.0) {
						// calculate the angle for polar coordinates
						/*
						double theta = Math.atan2(ny,nx);
						/*/
						double K = nr / (r * l);
						//*/
						// calculate new x position with new distance in same
						// angle
						double nxn = K * nx;// */nr*Math.cos(theta)/l;
						// calculate new y position with new distance in same
						// angle
						double nyn = K * ny;// */nr*Math.sin(theta)/l;
						// map from -1 ... 1 to image coordinates
						int x2 = (int) ((nxn + 1.0) * w_src_2);
						// map from -1 ... 1 to image coordinates
						int y2 = (int) ((nyn + 1.0) * h_src_2);
						// make sure that position stays within arrays
						if (0<=y2 && y2 < h_src && 0<=x2 && x2<w_src) {
							// get new pixel (x2,y2) and put it to target array
							// at (x,y)
							dstpixels[(int) (y * w + x)] = srcpixels[y2 * w_src + x2];
						}
						else
							dstpixels[(int)(y * w + x)] = 0;
					}
				}
			}
		}
		// return result pixels
		return dstpixels;
	}
}
