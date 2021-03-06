//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Assignment.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;

    public partial class HealthRecord
    {
        public int Id { get; set; }
        [Required(ErrorMessage = "Please enter mood .")]
        [RegularExpression("[a-zA-Z ]+$", ErrorMessage = "input can be letter and space only")]
        public string Mood { get; set; }
        [Required(ErrorMessage = "Please enter Blood Pressure.")]
        [Range(0, 200, ErrorMessage = "Please input the correct Blood Pressure.")]
        public int BloodPresure { get; set; }
        [Required(ErrorMessage = "Please enter Heart Rate.")]
        [Range(40  , 200, ErrorMessage = "Please input the correct Heart Rate.")]
        public int HeartRate { get; set; }
        [Required(ErrorMessage = "Please enter Pulse Rate.")]
        [Range(40, 200, ErrorMessage = "Please input the correct Pulse Rate.")]
        public int Pulse { get; set; }
        public System.DateTime Date { get; set; }
        public string AspNetUserId { get; set; }
    
        public virtual AspNetUser AspNetUser { get; set; }
    }
}
