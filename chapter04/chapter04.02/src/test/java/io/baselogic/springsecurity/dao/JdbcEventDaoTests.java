package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JdbcEventDaoTests
 *
 * @since chapter1.00
 */
@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class JdbcEventDaoTests {

    @Autowired
    private EventDao eventDao;

    private AppUser owner = new AppUser();
    private AppUser attendee = new AppUser();

    @BeforeEach
    void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }


    @Test
    void initJdbcOperations() {
        assertThat(eventDao).isNotNull();
    }

    @Test
    void find() {
        Event event = eventDao.findById(100);
        log.info(event.toString());

        assertThat(event).isNotNull();
        assertThat(event).isEqualTo(event);
        assertThat(event).isNotEqualTo(new Object());
        assertThat(event.equals(Event.builder().build())).isFalse();
        assertThat(event.hashCode()).isNotZero();

        assertThat(event.getSummary()).isEqualTo("Birthday Party");
        assertThat(event.getOwner().getId()).isEqualTo(0);
        assertThat(event.getAttendee().getId()).isEqualTo(1);
    }


    @Test
    void createEvent() {
        log.debug("******************************");
        List<Event> events = eventDao.findByUser(owner.getId());
        assertThat(events.size()).isGreaterThanOrEqualTo(2);

        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        int eventId = eventDao.save(event);

        List<Event> newEvents = eventDao.findByUser(owner.getId());
        assertThat(newEvents.size()).isGreaterThanOrEqualTo(3);
        // find eventId in List...
//        assertThat(newEvents.get(3)).isEqualTo(3);
    }

    @Test
    void createEvent_null_event() {
        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(null);
        });
    }

    @Test
    void createEvent_with_event_id() {
        assertThrows(IllegalArgumentException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setId(12345);
            eventDao.save(event);
        });

    }

    @Test
    void createEvent_null_event_owner() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setOwner(null);
            eventDao.save(event);
        });

    }

    @Test
    void createEvent_null_event_attendee() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setAttendee(null);
            eventDao.save(event);
        });

    }

    @Test
    void createEvent_null_event_when() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setWhen(null);
            eventDao.save(event);
        });

    }


    @Test
    void findAll() {
        List<Event> events = eventDao.findAll();
        assertThat(events.size()).isGreaterThanOrEqualTo(3);
    }

} // The End...
