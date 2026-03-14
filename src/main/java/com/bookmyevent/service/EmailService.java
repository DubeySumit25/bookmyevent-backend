package com.bookmyevent.service;

import com.bookmyevent.entity.Booking;
import com.bookmyevent.entity.Ticket;
import com.bookmyevent.repository.TicketRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final TicketRepository ticketRepository;

    @Async
    public void sendBookingConfirmation(Booking booking) {
        try {
            List<String> ticketNumbers = ticketRepository
                    .findByBookingId(booking.getId())
                    .stream()
                    .map(Ticket::getTicketNumber)
                    .toList();

            Context context = new Context();
            context.setVariable("userName", booking.getUser().getFullName());
            context.setVariable("eventTitle", booking.getEvent().getTitle());
            context.setVariable("eventVenue", booking.getEvent().getVenue());
            context.setVariable("eventDate", booking.getEvent().getEventDate().toString());
            context.setVariable("numberOfTickets", booking.getNumberOfTickets());
            context.setVariable("bookingReference", booking.getBookingReference());
            context.setVariable("totalAmount", booking.getTotalAmount());
            context.setVariable("ticketNumbers", ticketNumbers);

            String html = templateEngine.process("booking-confirmation", context);
            sendEmail(booking.getUser().getEmail(),
                    "🎉 Booking Confirmed - " + booking.getEvent().getTitle(), html);

            log.info("Confirmation email sent to {}", booking.getUser().getEmail());

        } catch (Exception e) {
            log.error("Failed to send confirmation email: {}", e.getMessage());
        }
    }

    @Async
    public void sendCancellationEmail(Booking booking) {
        try {
            Context context = new Context();
            context.setVariable("userName", booking.getUser().getFullName());
            context.setVariable("eventTitle", booking.getEvent().getTitle());
            context.setVariable("bookingReference", booking.getBookingReference());
            context.setVariable("numberOfTickets", booking.getNumberOfTickets());
            context.setVariable("totalAmount", booking.getTotalAmount());

            String html = templateEngine.process("booking-cancellation", context);
            sendEmail(booking.getUser().getEmail(),
                    "❌ Booking Cancelled - " + booking.getEvent().getTitle(), html);

            log.info("Cancellation email sent to {}", booking.getUser().getEmail());

        } catch (Exception e) {
            log.error("Failed to send cancellation email: {}", e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String html)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.setFrom("noreply@bookmyevent.com");
        mailSender.send(message);
    }
}