using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using Assignment.Models;
using Microsoft.AspNet.Identity;

namespace Assignment.Controllers
{
    [Authorize]
    public class AppointmentsController : Controller
    {
        private Entities1 db = new Entities1();
        private string userId;
        // GET: Appointments
        public ActionResult Index()
        {
            if (!User.IsInRole("admin"))
            {
                userId = User.Identity.GetUserId();
                var appointments = db.Appointments.Where(a => a.AspNetUserId ==
                userId).ToList();
                return View(appointments);
            }
            else 
            {
                return View(db.Appointments.ToList());
            }
        }

        // GET: Appointments/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Appointment appointment = db.Appointments.Find(id);
            if (appointment == null)
            {
                return HttpNotFound();
            }
            return View(appointment);
        }

        // GET: Appointments/Create
        public ActionResult Create()
        {
            ViewBag.TreatmentId = new SelectList(db.Treatments, "Id", "Name");
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email");
            return View();
        }

        // POST: Appointments/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,Date,Evaluation,TreatmentId")] Appointment appointment)
        {
            appointment.AspNetUserId = User.Identity.GetUserId();
            ModelState.Clear();
            TryValidateModel(appointment);
            if (ModelState.IsValid)
            {
                db.Appointments.Add(appointment);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.TreatmentId = new SelectList(db.Treatments, "Id", "Name", appointment.TreatmentId);
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", appointment.AspNetUserId);
            return View(appointment);
        }

        // GET: Appointments/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Appointment appointment = db.Appointments.Find(id);
            if (appointment == null)
            {
                return HttpNotFound();
            }
            ViewBag.rateValue = appointment.Evaluation;
            ViewBag.TreatmentId = new SelectList(db.Treatments, "Id", "Name", appointment.TreatmentId);
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", appointment.AspNetUserId);
            return View(appointment);
        }

        // POST: Appointments/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,Date,Evaluation,TreatmentId,AspNetUserId")] Appointment appointment)
        {
            ViewBag.id= User.Identity.GetUserId();
            if (ModelState.IsValid)
            {
                db.Entry(appointment).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.TreatmentId = new SelectList(db.Treatments, "Id", "Name", appointment.TreatmentId);
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", appointment.AspNetUserId);
            return View(appointment);
        }

        // GET: Appointments/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Appointment appointment = db.Appointments.Find(id);
            if (appointment == null)
            {
                return HttpNotFound();
            }
            return View(appointment);
        }

        // POST: Appointments/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Appointment appointment = db.Appointments.Find(id);
            db.Appointments.Remove(appointment);
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

        public ActionResult Evaluation(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Appointment appointment = db.Appointments.Find(id);
            if (appointment == null)
            {
                return HttpNotFound();
            }
            ViewBag.rateValue = appointment.Evaluation;
            ViewBag.TreatmentId = new SelectList(db.Treatments, "Id", "Name", appointment.TreatmentId);
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", appointment.AspNetUserId);
            return View(appointment);
        }

        // POST: Appointments/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details see https://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Evaluation([Bind(Include = "Id,Date,Evaluation,TreatmentId,AspNetUserId")] Appointment appointment)
        {

            string rating = Request.Params["input"];
            appointment.Evaluation = double.Parse(rating);
            if (ModelState.IsValid)
            {
                db.Entry(appointment).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.TreatmentId = new SelectList(db.Treatments, "Id", "Name", appointment.TreatmentId);
            ViewBag.AspNetUserId = new SelectList(db.AspNetUsers, "Id", "Email", appointment.AspNetUserId);
            return View(appointment);
        }
        public ActionResult Calendar(string date = "1234")
        {
            if ("1234" == date)
                return View();
            DateTime convertedDate = DateTime.Parse(date);
            ViewBag.eventDate = convertedDate;
            return View();
        }
    }
}
