package org.spatiallibs.jsi.util;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.Point;

import org.spatiallibs.common.ResourceLoader;

public class Utilities {

	/* Reading Points related functions */

	// reads a  binary file and coverts the byte array to a list of geometries
	public static List<Point> readBinaryPoints(String fileName) {
		byte[] data = ResourceLoader.readBinaryFile(fileName);
		List<Point> geoms = new ArrayList<Point>();
		int times = Double.SIZE / Byte.SIZE;
		for(int i = 0; i < (data.length / times); i = i + 2) {
			double x = ByteBuffer.wrap(data, i * times, times).getDouble();
			double y = ByteBuffer.wrap(data, ((i + 1) * times), times).getDouble();
			Point geom = new Point((float) x, (float) y);
			geoms.add(geom);
		}
		return geoms;
	}

	public static List<Rectangle> getEnvelopes(String fileName) {
		List<String> lines = ResourceLoader.readFileInList(fileName);
		Iterator<String> itr = lines.iterator();
		List<Rectangle> ranges = new ArrayList<Rectangle>();

		while (itr.hasNext()) {
			String line = itr.next();
			ArrayList<Float> coords = getCoords(line);
			Rectangle rect = new Rectangle(coords.get(0), coords.get(1), coords.get(2), coords.get(3));
			ranges.add(rect);
		}
		return ranges;
	}

	public static List<Point> getQueryPoints(String fileName) {
		List<String> lines = ResourceLoader.readFileInList(fileName);
		Iterator<String> itr = lines.iterator();

		List<Point> queryPoints = new ArrayList<Point>();

		while(itr.hasNext()) {
			String line = itr.next();
			ArrayList<Float> coords = getCoords(line);
			Point point = new Point(coords.get(0), coords.get(1));
			queryPoints.add(point);
		}

		return queryPoints;
	}

	public static List<Float> getDistances(String fileName) {
		List<String> lines = ResourceLoader.readFileInList(fileName);
		Iterator<String> itr = lines.iterator();
		List<Float> distances = new ArrayList<Float>();

		while (itr.hasNext()) {
			String line = itr.next();
			ArrayList<Float> coords = getCoords(line);
			distances.add(coords.get(2));
		}
		return distances;
	}

	/* Utility functions */

	// read coordinates from a string into a list
	public static ArrayList<Float> getCoords(String line) {
		ArrayList<Float> al = new ArrayList<Float>();
		StringTokenizer defaultTokenizer = new StringTokenizer(line, ",");
		while (defaultTokenizer.hasMoreTokens()) {
			String s = defaultTokenizer.nextToken();
			float d = Float.parseFloat(s);
			al.add(d);
		}
		return al;
	}

}
