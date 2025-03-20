package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@Import({ItemRequestMapper.class, UserMapper.class})
public class ItemRequestControllerTest {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ItemRequestMapper itemRequestMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Получить запрос")
    public void shouldGetItemRequest() throws Exception {
        ItemRequestWithItemsDto itemRequestWithItemsDto = TestData.getItemRequestWithItemsDto();

        when(itemRequestService.getItemById(itemRequestWithItemsDto.getId())).thenReturn(itemRequestWithItemsDto);

        mvc.perform(get("/requests/" + itemRequestWithItemsDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestWithItemsDto)));

        when(itemRequestService.getItemById(-1L)).thenThrow(NotFoundException.class);

        mvc.perform(get("/requests/" + -1))
                .andExpect(status().is4xxClientError());

        verify(itemRequestService, times(1)).getItemById(itemRequestWithItemsDto.getId());
        verify(itemRequestService, times(1)).getItemById(-1L);
    }

    @Test
    @DisplayName("Список всех запросов созданных пользователем с сортировкой")
    public void shouldGetItemRequestsByUser() throws Exception {
        User user = TestData.getUser();
        List<ItemRequestWithItemsDto> itemRequestWithItemsDto = TestData.getItemRequestsByUser();

        when(itemRequestService.getItemRequestsByUser(user.getId())).thenReturn(itemRequestWithItemsDto);

        mvc.perform(get("/requests")
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestWithItemsDto)));

        User user2 = TestData.getUser2();
        itemRequestWithItemsDto = List.of(TestData.getItemRequestWithItemsDto());

        when(itemRequestService.getItemRequestsByUser(user2.getId())).thenReturn(itemRequestWithItemsDto);

        mvc.perform(get("/requests")
                        .header(HEADER_USER, user2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestWithItemsDto)));

        verify(itemRequestService, times(1)).getItemRequestsByUser(user.getId());
        verify(itemRequestService, times(1)).getItemRequestsByUser(user2.getId());
    }

    @Test
    @DisplayName("Список всех запросов кроме созданных пользователем с сортировкой")
    public void shouldGetItemRequestsAll() throws Exception {
        User user = TestData.getUser();
        List<ItemRequestDto> itemRequestDto = List.of(itemRequestMapper.toItemRequestDto(TestData.getItemRequest()));

        when(itemRequestService.getItemRequestsAll(user.getId())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/all")
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));

        User user2 = TestData.getUser2();
        itemRequestDto = TestData.getFindByRequestorIdOrderByCreatedDesc().stream()
                .map(itemRequestMapper::toItemRequestDto)
                .toList();

        when(itemRequestService.getItemRequestsAll(user2.getId())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/all")
                        .header(HEADER_USER, user2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));

        verify(itemRequestService, times(1)).getItemRequestsAll(user.getId());
        verify(itemRequestService, times(1)).getItemRequestsAll(user2.getId());
    }

    @Test
    @DisplayName("Создать запрос")
    public void shouldCreateItemRequest() throws Exception {
        User user = TestData.getUser();
        RequestItemRequestDto requestItemRequestDto = new RequestItemRequestDto("new description");
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                null,
                requestItemRequestDto.getDescription(),
                userMapper.toUserDto(user),
                LocalDateTime.now());

        when(itemRequestService.createItemRequest(user.getId(), requestItemRequestDto)).thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));

        User wronfUser = TestData.getWrongUser();

        when(itemRequestService.createItemRequest(wronfUser.getId(), requestItemRequestDto))
                .thenThrow(NotFoundException.class);

        mvc.perform(post("/requests")
                        .header(HEADER_USER, wronfUser.getId())
                        .content(mapper.writeValueAsString(requestItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemRequestService, times(1)).createItemRequest(user.getId(), requestItemRequestDto);
        verify(itemRequestService, times(1)).createItemRequest(wronfUser.getId(), requestItemRequestDto);
    }
}
