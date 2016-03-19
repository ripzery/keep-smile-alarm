using System;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Data;
using System.Data.Entity.Validation;


namespace French.Services
{
    public interface IService<T> where T: class
    {
        IEnumerable<T> GetAll();        
        T GetByID(int id);        
    }
}
