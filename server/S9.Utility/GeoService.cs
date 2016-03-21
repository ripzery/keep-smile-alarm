using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;

namespace S9.Utility
{
    public static class GeoService
    {
        /// <summary>
        /// Calculate distance between 2 geo points
        /// </summary>
        /// <param name="lat1">lat of ponit1</param>
        /// <param name="lng1">lng of point1</param>
        /// <param name="lat2">lat of point1</param>
        /// <param name="lng2">lng of point2</param>
        /// <returns>distance between 2 points</returns>
        public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
        {
            double dist = Math.Sin(deg2rad(lat1)) * Math.Sin(deg2rad(lat2)) +
                          Math.Cos(deg2rad(lat1)) * Math.Cos(deg2rad(lat2)) * Math.Cos(deg2rad(lng1 - lng2));

            // Convert to degree
            dist = rad2deg(Math.Acos(dist));
            if(Double.IsNaN(dist))
                return 0;

            // Convert to (minute and to) mile
            dist = dist * 60 * 1.1515;

            // Convert to KM
            dist = dist * 1.609344;

            // Convert to mile
            //dist = dist * 0.8684;

            return dist;
        }

        /// <summary>
        /// Convert value from degree to radian
        /// </summary>
        /// <param name="deg"></param>
        /// <returns>value in radian</returns>
        private static double deg2rad(double deg)
        {
            return (deg * Math.PI / 180.0);
        }

        /// <summary>
        /// Convert value from radian to degree
        /// </summary>
        /// <param name="rad"></param>
        /// <returns>value in degree</returns>
        private static double rad2deg(double rad)
        {
            return (rad / Math.PI * 180.0);
        }  
    }
}
