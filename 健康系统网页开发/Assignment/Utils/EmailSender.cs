using SendGrid;
using SendGrid.Helpers.Mail;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace Assignment.Utils
{
    public class EmailSender
    {
        // Please use your API KEY here.
        private const String API_KEY = "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";

        public void Send(String toEmail, String subject, String contents, HttpPostedFileBase postedFileBase)
            
        {
            var client = new SendGridClient(API_KEY);
            var from = new EmailAddress("17791483727@163.com", "FIT5032 Example Email User");
            var to = new EmailAddress(toEmail, "");
            var plainTextContent = contents;
            var htmlContent = "<p>" + contents + "</p>";
            var msg = MailHelper.CreateSingleEmail(from, to, subject, plainTextContent, htmlContent);
            if (postedFileBase != null && postedFileBase.ContentLength > 0)
            {
                string theFileName = Path.GetFileName(postedFileBase.FileName);
                byte[] fileBytes = new byte[postedFileBase.ContentLength];
                using (BinaryReader theReader = new BinaryReader(postedFileBase.InputStream))
                {
                    fileBytes = theReader.ReadBytes(postedFileBase.ContentLength);
                }
                string dataAsString = Convert.ToBase64String(fileBytes);
                msg.AddAttachment(theFileName, dataAsString);
            }
            var response = client.SendEmailAsync(msg);
        }
        public void SendMutiple(String toEmail, String subject, String contents, HttpPostedFileBase postedFileBase)

        {
            var client = new SendGridClient(API_KEY);
            var from = new EmailAddress("17791483727@163.com", "FIT5032 Example Email User");
            //var to = new EmailAddress(toEmail, "");
            var plainTextContent = contents;
            var htmlContent = "<p>" + contents + "</p>";

            List<string> list = toEmail.Split(';').ToList<string>();
            List<EmailAddress> emails = new List<EmailAddress>();
            foreach (var email in list) 
            {
                emails.Add(new EmailAddress(email, ""));
            }
           
            
            var msg = MailHelper.CreateSingleEmailToMultipleRecipients(from, emails,subject,plainTextContent,htmlContent);


            if (postedFileBase != null && postedFileBase.ContentLength > 0)
            {
                string theFileName = Path.GetFileName(postedFileBase.FileName);
                byte[] fileBytes = new byte[postedFileBase.ContentLength];
                using (BinaryReader theReader = new BinaryReader(postedFileBase.InputStream))
                {
                    fileBytes = theReader.ReadBytes(postedFileBase.ContentLength);
                }
                string dataAsString = Convert.ToBase64String(fileBytes);
                msg.AddAttachment(theFileName, dataAsString);
            }
            var response = client.SendEmailAsync(msg);
        }

    }
}