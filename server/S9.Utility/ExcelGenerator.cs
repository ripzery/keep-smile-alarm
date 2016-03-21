using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.IO;



// ***** R E Q U I R E *******
// add 'DocumentFormat.OpenXml' to the references
// add WindowsBase 'WindowsBased' to the references


// OpenXML for exporting data to Excel 
using DocumentFormat.OpenXml;
using DocumentFormat.OpenXml.Packaging;
using DocumentFormat.OpenXml.Spreadsheet;

// For getting class/object properties
using System.Reflection;


namespace S9.Utility
{    
    // to support type generic format (can be used with any type)
    public class ExcelGenerator<T>
    {
        // member variables
        private string _fileName;
        private string _sheetName;
        //private bool _isIO; // memeory
        private List<T> _data;
        
        // constructor
        public ExcelGenerator(string fileName, string sheetName, List<T> data)
        {
            // store data into the member variable
            _fileName = fileName;
            _sheetName = sheetName;
            _data = data;
        }

        public MemoryStream GetStream()
        {
            MemoryStream s = new MemoryStream();

            // open/load the file, prepare document
            SpreadsheetDocument spreadsheetDocument = SpreadsheetDocument.Create(s, SpreadsheetDocumentType.Workbook);

            Save(spreadsheetDocument);
            spreadsheetDocument.Close();

            return s;
        }


        public bool Save()
        {
            bool bResult = false;

            // open/load the file, prepare document
            SpreadsheetDocument spreadsheetDocument = SpreadsheetDocument.Create(_fileName, SpreadsheetDocumentType.Workbook);
            bResult = Save(spreadsheetDocument);
            spreadsheetDocument.Close();
            
            return bResult;
        }


        private bool Save(SpreadsheetDocument spreadsheetDocument)
        {
            WorkbookPart workbookpart = spreadsheetDocument.AddWorkbookPart();
            workbookpart.Workbook = new Workbook();

            WorksheetPart worksheetPart = workbookpart.AddNewPart<WorksheetPart>();

            SheetData sd = new SheetData();
            worksheetPart.Worksheet = new Worksheet(sd);

            Sheets sheets = spreadsheetDocument.WorkbookPart.Workbook.AppendChild<Sheets>(new Sheets());
            Sheet sheet = new Sheet()
            {
                Id = spreadsheetDocument.WorkbookPart.GetIdOfPart(worksheetPart),
                SheetId = 1,
                Name = _sheetName
            };
            sheets.Append(sheet);

            // 2. write the header
            Row rowHeader = new Row();

            PropertyInfo[] propertyInfos = typeof(T).GetProperties(BindingFlags.Public | BindingFlags.Instance);
            int iColumn = propertyInfos.Count();
            for (int i = 0; i < iColumn; i++)
            {
                Cell cellH = new Cell();

                // split type and field name
                string[] strFields;
                string strFieldName = "";
                strFields = propertyInfos[i].ToString().Split(' ');

                if (strFields.Length > 1)
                    strFieldName = strFields[1];
                else
                    strFieldName = strFields[0];

                cellH.DataType = CellValues.InlineString;
                cellH.InlineString = new InlineString() { Text = new Text(strFieldName) };

                rowHeader.Append(cellH);
            }
            sd.Append(rowHeader);




            // 3. write record(s) of data
            foreach (T p in _data)
            {
                Row rowData = new Row(); //{ RowIndex = (UInt32Value)2U };

                foreach (PropertyInfo prop in propertyInfos)
                {
                    object propValue = prop.GetValue(p, null);

                    Cell cell = new Cell();

                    // split the field type and field name
                    string[] strFields;
                    string strFieldType = "";
                    string strFieldName = "";
                    strFields = prop.ToString().Split(' ');

                    if (strFields.Length > 1)
                    {
                        strFieldType = strFields[0];
                        strFieldName = strFields[1];
                    }
                    else
                        strFieldName = strFields[0];

                    if (strFieldType.ToLower().Contains("int"))
                    {
                        try
                        {
                            // int
                            cell.DataType = new EnumValue<CellValues>(CellValues.Number);
                            cell.CellValue = new CellValue(propValue.ToString());
                        }
                        catch (Exception)
                        {

                        }
                    }
                    else
                    {
                        try
                        {
                            // others - string, datetime, boolean
                            cell.DataType = CellValues.InlineString;
                            cell.InlineString = new InlineString() { Text = new Text(propValue.ToString()) };
                        }
                        catch (Exception)
                        {

                        }
                    }

                    rowData.Append(cell);
                }

                sd.Append(rowData);
            }



            // Close the document
            workbookpart.Workbook.Save();            
            return true;
        }

   

    }
}
