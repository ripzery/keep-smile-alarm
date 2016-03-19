using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;

using System.Net;
using System.Net.Http;

using French.Services;
using French.Model;

using S9.Utility;

namespace French.Web.Controllers
{
    /// <summary>
    /// 
    /// </summary>
    public class MainController : Controller
    {
        //private static string version = WebContext.AppVersion;
        //private UserService userService = new UserService();

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        [AllowAnonymous]
        public ActionResult Index()
        {
            return View();
        }

        /// <summary>
        /// 
        /// </summary>
        [HttpGet]
        [AllowAnonymous]
        //[ActionSessionState(System.Web.SessionState.SessionStateBehavior.Required)]
        public void GetDistanceMatrix(string source, string destination)
        {
            string json = "";

            using (WebClient webClient = new WebClient())
            {
                webClient.Encoding = System.Text.Encoding.UTF8;
                string URL = WebContext.GoogleAPIURL;
                URL = URL.Replace("[SOURCE]", source);
                URL = URL.Replace("[DESTINATION]", destination);
                URL = URL.Replace("[KEY]", WebContext.GoogleAPIKey);
                json = webClient.DownloadString(URL);
            }



            Response.ClearHeaders();
            Response.ClearContent();
            Response.Clear();
            Response.Buffer = true;
            Response.ContentType = "application/json";
            //Response.AddHeader("content-disposition", "attachment; filename=" + fileName);
            //Response.Charset = "";
            Response.Write(json);
            Response.Flush();

            Response.End();
        }
        
    }

}
