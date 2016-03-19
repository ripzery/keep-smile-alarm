using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.Http;
using System.Net.Http;
using System.Net;
using System.Text;

using French.Services;

using French.Model;
using S9.Utility;

namespace French.Web.Controllers
{

    /// <summary>
    /// 
    /// </summary>
    public class BaseAPIController : ApiController
    {
        /*/
        /// <summary>
        /// Get a single item
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpGet]
        public IHttpActionResult Get(int id)
        {
            T obj = (T)sv.GetByID(id);
            return Ok(obj);
        }
        /*/

        /*/
        /// <summary>
        /// List of all items
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public IEnumerable<T> List()
        {
            IEnumerable<T> lst = sv.GetAll();
            return lst;
        }
        /*/

        /// <summary>
        /// function that return object as json
        /// </summary>
        /// <param name="result"></param>
        /// <returns>IHttpActionResult</returns>
        public IHttpActionResult JsonResp(object result)
        {
            //init HttpResponseMessage 
            var resp = new HttpResponseMessage()
            {
                //Serialize object to json format and encoding as UTF8
                Content = new StringContent(JSON<string>.SerializeObject(result), Encoding.UTF8, "application/json")
            };
            //return HttpResponseMessage
            return ResponseMessage(resp);
        }        
    }
}
