package codeplays.trainee.store.domain.order;

import codeplays.trainee.store.domain.payment.PaymentService;
import codeplays.trainee.store.domain.user.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private PaymentService paymentService = mock(PaymentService.class);
    private UserService userService = mock(UserService.class);
    private OrderService orderService = new OrderService(userService, paymentService);

    @Test
    public void shouldThrowsAnExceptionWhenUserIsMinor() {
        // Arrange
        Order order = new Order(1L);

        when(userService.isUserMinor(1L)).thenReturn(true);
        doNothing().when(paymentService).pay();

        // Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.create(order));

        // Assert
        assertEquals("O usuário não pode ser menor de idade", exception.getMessage());

        verify(userService, times(1)).isUserMinor(eq(1L));
        verify(paymentService, times(0)).pay();
    }

    @Test
    public void shouldPayWhenUserMajor() {
        // Arrange
        Order order = new Order(2L);

        when(userService.isUserMinor(eq(2L))).thenReturn(false);
        doNothing().when(paymentService).pay();

        // Act
        orderService.create(order);

        // Assert
        verify(userService, times(1)).isUserMinor(2L);
        verify(paymentService, times(1)).pay();
    }

}
