package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            select b
            from Booking as b
            where b.item.owner.id = ?1
            and case when ?2 = 'ALL' then 1
            when ?2 = 'CURRENT' and b.status = 'APPROVED' and CURRENT_DATE between b.start and b.end then 1
            when ?2 = 'PAST' and b.status = 'APPROVED' and b.end < CURRENT_DATE then 1
            when ?2 = 'FUTURE' and b.status = 'APPROVED' and b.start > CURRENT_DATE then 1
            when ?2 = 'WAITING' and b.status = 'WAITING' then 1
            when ?2 = 'REJECTED' and b.status = 'REJECTED' then 1
            else 0
            end = 1
            """)
    List<Booking> findByOwner(Long ownerId, String state);

    @Query("""
            select b
            from Booking as b
            where b.booker.id = ?1
            and case when ?2 = 'ALL' then 1
            when ?2 = 'CURRENT' and b.status = 'APPROVED' and CURRENT_TIMESTAMP between b.start and b.end then 1
            when ?2 = 'PAST' and b.status = 'APPROVED' and b.end < CURRENT_TIMESTAMP then 1
            when ?2 = 'FUTURE' and b.status = 'APPROVED' and b.start > CURRENT_TIMESTAMP then 1
            when ?2 = 'WAITING' and b.status = 'WAITING' then 1
            when ?2 = 'REJECTED' and b.status = 'REJECTED' then 1
            else 0
            end = 1
            order by b.start desc
            """)
    List<Booking> findByBooker(Long bookerId, String state);

    @Query("""
            select b
            from Booking as b
            where b.booker.id = ?1
            and b.item.id = ?2
            and b.status = 'APPROVED'
            and b.end <= CURRENT_TIMESTAMP
            order by b.end desc
            """)
    Optional<Booking> findPastBooking(Long userId, Long itemId);

    @Query("""
            select b
            from Booking as b
            where b.id in (select bl.id
            from (select bb.id as id,
            row_number() over(partition by bb.item.id order by bb.start desc) as rn
            from Booking as bb
            where bb.item.owner.id = ?1
            and bb.item.id in (?2)
            and bb.status = 'APPROVED'
            and bb.start <= CURRENT_TIMESTAMP) as bl
            where bl.rn = 1)
            """)
    List<Booking> findLastBooking(Long userId, List<Long> itemIds);

    @Query("""
            select b
            from Booking as b
            where b.id in (select bl.id
            from (select bb.id as id,
            row_number() over(partition by bb.item.id order by bb.start asc) as rn
            from Booking as bb
            where bb.item.owner.id = ?1
            and bb.item.id in (?2)
            and bb.status = 'APPROVED'
            and bb.start > CURRENT_TIMESTAMP) as bl
            where bl.rn = 1)
            """)
    List<Booking> findNextBooking(Long userId, List<Long> itemIds);

    void deleteByItemOwnerId(Long userId);
}