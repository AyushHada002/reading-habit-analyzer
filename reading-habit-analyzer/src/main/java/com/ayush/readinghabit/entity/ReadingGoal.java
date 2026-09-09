package com.ayush.readinghabit.entity;

import jakarta.persistence.*;

import java.time.YearMonth;
import com.ayush.readinghabit.config.YearMonthConverter;

@Entity
@Table(
        name = "reading_goals",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "goal_month"}
                )
        }
)
public class ReadingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = YearMonthConverter.class)
    @Column(
            name = "goal_month",
            nullable = false,
            length = 7
    )
    private YearMonth month;

    @Column(
            name = "target_pages",
            nullable = false
    )
    private Integer targetPages;

    @Column(
            name = "target_minutes",
            nullable = false
    )
    private Integer targetMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    public ReadingGoal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public Integer getTargetPages() {
        return targetPages;
    }

    public void setTargetPages(Integer targetPages) {
        this.targetPages = targetPages;
    }

    public Integer getTargetMinutes() {
        return targetMinutes;
    }

    public void setTargetMinutes(Integer targetMinutes) {
        this.targetMinutes = targetMinutes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
