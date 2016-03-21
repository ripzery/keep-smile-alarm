#define TEST

using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;
using Swashbuckle.Application;

namespace French.Web
{
    /// <summary>
    /// 
    /// </summary>
    public static class WebApiConfig
    {
        
        /// <summary>
        /// 
        /// </summary>
        /// <param name="config"></param>
        public static void Register(HttpConfiguration config)
        {
#if TEST
            config
              .EnableSwagger(c =>
              {
                  c.Schemes(new[] { "http", "https" });
                  c.SingleApiVersion("v1", "French");
                  c.IncludeXmlComments(string.Format(@"{0}\bin\French.API.XML",
                                       System.AppDomain.CurrentDomain.BaseDirectory));
              })
              .EnableSwaggerUi();
#endif


            config.MapHttpAttributeRoutes();

            // Attribute routing.
            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );

            // only JSON format
            config.Formatters.Remove(config.Formatters.XmlFormatter);
        }
    }
}
