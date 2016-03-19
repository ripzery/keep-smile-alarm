using System;
using System.Collections;
using System.Collections.Specialized;
using System.Collections.Generic;
using System.Linq;
using System.Text;
//using System.Text.RegularExpressions;
//using System.IO;

using French.Model;
using S9.Utility;

namespace French.Services
{
    // helper in business logic
    public class Helper
    {
        // property for controller to set        
        public static string DisplayDateFormat
        {
            get
            {
                return "dd.MM.yyyy";                
            }
        }
            
        public static string DisplayTimeFormat
        {
            get
            {
                return "HH:mm:ss";
            }
        }

        public static string DateTimeSignature
        {
            get
            {
                DateTime dtNow = DateTime.UtcNow;
                return dtNow.ToString( "yyyyMMddHHmmss" );
            }
        }

        public static int ScoreScale
        {
            get
            {
                return 5;
            }
        }

        public static string GetPaddedID(int ID)
        {
            return ID.ToString().PadLeft(6, '0');
        }



        public static string GetThumbnailFileName(string imageFileName)
        {
            return FileUtil.GetFileNameWithoutExtension(imageFileName) + "_t" + FileUtil.GetFileExtension(imageFileName);
        }


        /*/
        public static string GetImageRejectFlagString(int flag)
        {
            string strResult = "";

            foreach (Navigo_Enum.PHOTO_REJECT_REASON_FLAG f in EnumList<Navigo_Enum.PHOTO_REJECT_REASON_FLAG>.List())
            {
                // concatenate string
                if (0 < (flag & (int)f))
                    strResult += f.ToDescription() + " ";
            }

            return strResult;
        }
        /*/        
    }
}