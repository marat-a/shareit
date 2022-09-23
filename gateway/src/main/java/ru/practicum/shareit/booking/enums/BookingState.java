package ru.practicum.shareit.booking.enums;

import ru.practicum.shareit.common.exceptions.BadRequestException;

public enum BookingState {
	// Все
	ALL,
	// Текущие
	CURRENT,
	// Будущие
	FUTURE,
	// Завершенные
	PAST,
	// Отклоненные
	REJECTED,
	// Ожидающие подтверждения
	WAITING;

	public static BookingState from(String stringState) {
		for (BookingState state : values()) {
			if (state.name().equalsIgnoreCase(stringState)) {
				return state;
			}
		}
		throw new BadRequestException("Unknown state: " + stringState);
	}
}
