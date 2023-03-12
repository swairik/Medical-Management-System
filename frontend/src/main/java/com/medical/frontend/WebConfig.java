package com.medical.frontend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements  WebMvcConfigurer {
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/Admin").setViewName("/Admin/AdminDashboard.html");
        registry.addViewController("/AddDoctor").setViewName("/Admin/AddDoctor.html");
        registry.addViewController("/Doctor").setViewName("/Doctor/DoctorDashboard.html");
        registry.addViewController("/UpdateSchedule").setViewName("/Doctor/UpdateYourSchedule.html");
        registry.addViewController("/ViewYourAppointments").setViewName("/Doctor/ViewYourAppointments.html");
        registry.addViewController("/YourPatients").setViewName("/Doctor/YourPatients.html");
        registry.addViewController("/AddPrescription").setViewName("/Doctor/AddPrescriptionForm.html");
        registry.addViewController("/Patient").setViewName("/Patient/HomePage.html");
        registry.addViewController("/Profile").setViewName("/Patient/Profile.html");
        registry.addViewController("/PatientSignUp").setViewName("/Patient/SignUp.html");
        registry.addViewController("/SignIn").setViewName("/Patient/SignIn.html");
    }
}
