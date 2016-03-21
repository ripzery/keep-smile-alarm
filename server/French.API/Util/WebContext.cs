using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Security.Principal;
using System.Web.Security;
using System.Globalization;

using System.Configuration;

namespace French.Web
{
    /// <summary>
    /// 
    /// </summary>
    public class WebContext
    {
        /// <summary>
        /// 
        /// </summary>
        public static string AppVersion
        {
            get
            {
                return ConfigurationManager.AppSettings["APP_Version"].ToString();
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public static int PageSize
        {
            get 
            {
                int iResult = 0;
                int.TryParse(ConfigurationManager.AppSettings["PageSize"].ToString(), out iResult);
                return iResult;
            }
        }


        /// <summary>
        /// Google Matrix API URL
        /// </summary>
        public static string GoogleAPIURL
        {
            get
            {
                return ConfigurationManager.AppSettings["GoogleAPIURL"].ToString();
            }
        }




        /// <summary>
        /// Google Directions and Google Places
        /// </summary>
        public static string GoogleAPIKey
        {
            get
            {
                return ConfigurationManager.AppSettings["GoogleAPIKey"].ToString();
            }
        }

        
        /// <summary>
        /// Convert the provided app-relative path into an full physical path 
        /// </summary>
        /// <param name="relativePath">App-Relative path</param>
        /// <returns>Provided relativeUrl parameter as fully qualified Url</returns>
        /// <example>~\path\to\foo to c:\web\path\to\foo</example>
        public static string ToAbsolutePath(string relativePath)
        {
            if (HttpContext.Current == null)
                return relativePath;

            string filepath = HttpContext.Current.Request.PhysicalApplicationPath;        
            relativePath = relativePath.Replace("~", "");

            return filepath + relativePath;
        }
            


        /// <summary>
        /// Convert the provided app-relative path into an absolute Url containing the 
        /// full host name
        /// </summary>
        /// <param name="relativeUrl">App-Relative path</param>
        /// <returns>Provided relativeUrl parameter as fully qualified Url</returns>
        /// <example>~/path/to/foo to http://www.web.com/path/to/foo</example>
        private static string ToAbsoluteUrl(string relativeUrl)
        {
            if (string.IsNullOrEmpty(relativeUrl))
                return relativeUrl;

            if (HttpContext.Current == null)
                return relativeUrl;

            if (relativeUrl.StartsWith("/"))
                relativeUrl = relativeUrl.Insert(0, "~");
            if (!relativeUrl.StartsWith("~/"))
                relativeUrl = relativeUrl.Insert(0, "~/");

            var url = HttpContext.Current.Request.Url;
            var port = url.Port != 80 ? (":" + url.Port) : String.Empty;

            return String.Format("{0}://{1}{2}{3}",
                url.Scheme, url.Host, port, VirtualPathUtility.ToAbsolute(relativeUrl));
        }


    }
}

