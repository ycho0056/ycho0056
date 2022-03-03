using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using Assignment.Models;
using Assignment.Utils;
using Microsoft.AspNet.Identity;
using Service.Models;
using Newtonsoft.Json;

namespace Assignment.Controllers
{
    [Authorize]
    public class HealthRecordsController : Controller
    {
        private Entities1 db = new Entities1();

        private string userId;
        // GET: HealthRecords
        [Authorize]
        public ActionResult Index()
        {
            if (!User.IsInRole("admin"))
            {
                userId = User.Identity.GetUserId();
                var healthRecords = db.HealthRecords.Where(h => h.AspNetUserId ==
                userId).ToList();
                List<DataPoint> dataPoints1 = new List<DataPoint>();
                List<DataPoint> dataPoints2 = new List<DataPoint>();
                List<DataPoint> dataPoints3 = new List<DataPoint>();
                foreach(var record in healthRecords)
                {
                    dataPoints1.Add(new DataPoint(record.Date.ToString(), record.BloodPresure));
                    dataPoints2.Add(new DataPoint(record.Date.ToString(), record.HeartRate));
                    dataPoints3.Add(new DataPoint(record.Date.ToString(), record.Pulse));
                }

                ViewBag.DataPoints1 = JsonConvert.SerializeObject(dataPoints1);
                ViewBag.DataPoints2 = JsonConvert.SerializeObject(dataPoints2);
                ViewBag.DataPoints3 = JsonConvert.SerializeObject(dataPoints3);
                return View(healthRecords);
            }
            else 
            {
                return View(db.HealthRecords.ToList());
            }
        }

        // GET: HealthRecords/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            HealthRecord healthRecord = db.HealthRecords.Find(id);
            if (healthRecord == null)
            {
                return HttpNotFound();
            }
            return View(healthRecord);
        }

        // GET: HealthRecords/Create
        public ActionResult Create()
        {
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email");
            return View();
        }

        // POST: HealthRecords/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,Mood,BloodPresure,HeartRate,Pulse,Date")] HealthRecord healthRecord)
        {
            healthRecord.AspNetUserId = User.Identity.GetUserId();
            ModelState.Clear();
            TryValidateModel(healthRecord);
            if (ModelState.IsValid)
            {
                db.HealthRecords.Add(healthRecord);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", healthRecord.AspNetUserId);
            return View(healthRecord);
        }

        // GET: HealthRecords/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            HealthRecord healthRecord = db.HealthRecords.Find(id);
            if (healthRecord == null)
            {
                return HttpNotFound();
            }
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", healthRecord.AspNetUserId);
            return View(healthRecord);
        }

        // POST: HealthRecords/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,Mood,BloodPresure,HeartRate,Pulse,Date,AspNetUserId")] HealthRecord healthRecord)
        {
            if (ModelState.IsValid)
            {
                db.Entry(healthRecord).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", healthRecord.AspNetUserId);
            return View(healthRecord);
        }

        // GET: HealthRecords/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            HealthRecord healthRecord = db.HealthRecords.Find(id);
            if (healthRecord == null)
            {
                return HttpNotFound();
            }
            return View(healthRecord);
        }

        // POST: HealthRecords/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            HealthRecord healthRecord = db.HealthRecords.Find(id);
            db.HealthRecords.Remove(healthRecord);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
        public ActionResult Send_Email()
        {
            return View(new SendEmailViewModel());
        }

        [HttpPost]
        public ActionResult Send_Email(SendEmailViewModel model)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    String toEmail = model.ToEmail;
                    String subject = model.Subject;
                    String contents = model.Contents;
                    HttpPostedFileBase postedFileBase = model.postedFileBase;
                    EmailSender es = new EmailSender();
                    if (toEmail.Contains(";"))
                    {
                        es.SendMutiple(toEmail, subject, contents, postedFileBase);
                        
                    }
                    else 
                    {
                        es.Send(toEmail, subject, contents, postedFileBase);
                    }
                    ViewBag.Result = "Email has been send.";

                    ModelState.Clear();

                    return View(new SendEmailViewModel());
                }
                catch
                {
                    return View();
                }
            }

            return View();
        }
    }
}
