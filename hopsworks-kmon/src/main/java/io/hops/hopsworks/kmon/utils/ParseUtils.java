/*
 * This file is part of HopsWorks
 *
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved.
 *
 * HopsWorks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HopsWorks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with HopsWorks.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.hops.hopsworks.kmon.utils;

import java.text.DecimalFormat;
import java.text.ParseException;

public class ParseUtils {

  public static Double parseDouble(String d) throws ParseException {
    DecimalFormat format = new DecimalFormat("#.##");
    return format.parse(d.toUpperCase().replace("+", "")).doubleValue();
  }

  public static long parseLong(String d) throws ParseException {
    DecimalFormat format = new DecimalFormat("#.##");
    return format.parse(d.toUpperCase().replace("+", "")).longValue();
  }

  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
