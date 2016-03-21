using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Security.Cryptography;

namespace S9.Utility
{
    public class CryptographyUtil
    {
        #region "Hash Cryptography"
        ////**************
        //การเข้ารหัสแบบ hash จะไม่สามารถทำให้ข้อมูลเหมือนเดิมได้ลักษณะการใช้งานคือ
        //เก็บไว้ในฐานข้อมูลแล้วเปรียบกับข้อมูลที่ส่งเข้าว่าเหมือนกันหรือไม่
        //Algorithms     size digest
        //MD5
        //SHA1           28 byte
        //SHA256         44 byte
        //SHA384         64 byte
        //SHA512         88 byte
        ////***************
        public string EncryptedMD5(string txtdata)
        {
            MD5CryptoServiceProvider md5Hasher = new MD5CryptoServiceProvider();
            UTF8Encoding encoder = new UTF8Encoding();
            byte[] hashedBytes = encoder.GetBytes(txtdata);
            //Dim hashedBytes() As Byte = Encoding.ASCII.GetBytes(txtdata)
            byte[] encryptedData = md5Hasher.ComputeHash(hashedBytes);
            string txtencrypted = null;
            txtencrypted = Convert.ToBase64String(encryptedData);
            return txtencrypted;
        }

        public string EncryptedSHA1(string txtdata)
        {
            SHA1Managed sha1Hasher = new SHA1Managed();
            byte[] hashedBytes = Encoding.ASCII.GetBytes(txtdata);
            byte[] encryptedData = sha1Hasher.ComputeHash(hashedBytes);
            string txtencrypted = null;
            txtencrypted = Convert.ToBase64String(encryptedData);
            return txtencrypted;
        }

        public string EncryptedSHA256(string txtdata)
        {
            SHA256Managed sha256Hasher = new SHA256Managed();
            byte[] hashedBytes = Encoding.ASCII.GetBytes(txtdata);
            byte[] encryptedData = sha256Hasher.ComputeHash(hashedBytes);
            string txtencrypted = null;
            txtencrypted = Convert.ToBase64String(encryptedData);
            return txtencrypted;
        }

        public string EncryptedSHA384(string txtdata)
        {
            SHA384Managed sha384Hasher = new SHA384Managed();
            byte[] hashedBytes = Encoding.ASCII.GetBytes(txtdata);
            byte[] encryptedData = sha384Hasher.ComputeHash(hashedBytes);
            string txtencrypted = null;
            txtencrypted = Convert.ToBase64String(encryptedData);
            return txtencrypted;
        }

        public string EncryptedSHA512(string txtdata)
        {
            SHA512Managed sha512Hasher = new SHA512Managed();
            byte[] hashedBytes = Encoding.ASCII.GetBytes(txtdata);
            byte[] encryptedData = sha512Hasher.ComputeHash(hashedBytes);
            string txtencrypted = null;
            txtencrypted = Convert.ToBase64String(encryptedData);
            return txtencrypted;
        }

        #endregion

    }
}
