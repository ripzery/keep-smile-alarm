#define TEST

using System.Web.Http;
using WebActivatorEx;
using Swashbuckle.Application;
using French.Web;


#if TEST
[assembly: PreApplicationStartMethod(typeof(SwaggerConfig), "Register")]

namespace French.Web
{
    /// <summary>
    /// 
    /// </summary>
    public class SwaggerConfig
    {
        /// <summary>
        /// 
        /// </summary>
        public static void Register()
        {
            var thisAssembly = typeof (SwaggerConfig).Assembly;
        }
    }
}
#endif