package ru.practicum.shareit.item;

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
import ru.practicum.shareit.exception.CRUDException;
import ru.practicum.shareit.exception.ChekParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@Import({ItemMapper.class, UserMapper.class})
public class ItemControllerTest {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Получить вещь")
    public void shouldGetItem() throws Exception {
        User user = TestData.getUser();
        User user2 = TestData.getUser2();
        User wrongUser = TestData.getWrongUser();
        ItemInfoDto itemInfoDto = TestData.getItemInfoDto();

        when(itemService.getItem(user.getId(), itemInfoDto.getId())).thenReturn(itemInfoDto);

        mvc.perform(get("/items/" + itemInfoDto.getId())
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemInfoDto)));

        when(itemService.getItem(wrongUser.getId(), itemInfoDto.getId())).thenThrow(NotFoundException.class);

        mvc.perform(get("/items/" + itemInfoDto.getId())
                        .header(HEADER_USER, wrongUser.getId()))
                .andExpect(status().is4xxClientError());

        when(itemService.getItem(user.getId(), -1L)).thenThrow(NotFoundException.class);

        mvc.perform(get("/items/" + -1)
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().is4xxClientError());

        itemInfoDto = itemInfoDto.toBuilder()
                .lastBooking(null)
                .nextBooking(null)
                .build();

        when(itemService.getItem(user2.getId(), itemInfoDto.getId())).thenReturn(itemInfoDto);

        mvc.perform(get("/items/" + itemInfoDto.getId())
                        .header(HEADER_USER, user2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemInfoDto)));

        verify(itemService, times(1)).getItem(user.getId(), itemInfoDto.getId());
        verify(itemService, times(1)).getItem(wrongUser.getId(), itemInfoDto.getId());
        verify(itemService, times(1)).getItem(user.getId(), -1L);
        verify(itemService, times(1)).getItem(user2.getId(), itemInfoDto.getId());
    }

    @Test
    @DisplayName("Получить вещи по владельцу")
    public void shouldGetItemByUser() throws Exception {
        User user = TestData.getUser();
        List<ItemInfoDto> itemInfoDto = TestData.getItemsInfoDtoByOwner();

        when(itemService.getItemByUser(user.getId())).thenReturn(itemInfoDto);

        mvc.perform(get("/items")
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemInfoDto)));

        verify(itemService, times(1)).getItemByUser(user.getId());
    }

    @Test
    @DisplayName("Найти вещи")
    public void shouldSearchItem() throws Exception {
        User user = TestData.getUser();
        List<ItemDto> itemDto = List.of(itemMapper.toItemDto(TestData.getItem()));

        when(itemService.searchItem("DeScR")).thenReturn(itemDto);

        mvc.perform(get("/items/search")
                        .header(HEADER_USER, user.getId())
                        .param("text", "DeScR"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));

        verify(itemService, times(1)).searchItem("DeScR");
    }

    @Test
    @DisplayName("Создать вещь")
    public void shouldCreateItem() throws Exception {
        User user = TestData.getUser();
        RequestItemDto requestItemDto = new RequestItemDto(
                "new item",
                "new description",
                true,
                null);
        ItemDto itemDto = new ItemDto(
                null,
                requestItemDto.getName(),
                requestItemDto.getDescription(),
                userMapper.toUserDto(user),
                requestItemDto.getAvailable());

        when(itemService.createItem(user.getId(), requestItemDto)).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));

        verify(itemService, times(1)).createItem(user.getId(), requestItemDto);
    }

    @Test
    @DisplayName("Изменить вещь")
    public void shouldUpdateItem() throws Exception {
        User user = TestData.getUser();
        User user2 = TestData.getUser2();
        Item item = TestData.getItem();
        RequestItemDto requestItemDto = new RequestItemDto(
                "new item",
                "new description",
                true,
                null);
        ItemDto itemDto = itemMapper.toItemDto(item).toBuilder()
                .name(requestItemDto.getName())
                .description(requestItemDto.getDescription())
                .available(requestItemDto.getAvailable())
                .build();

        when(itemService.updateItem(user.getId(), item.getId(), requestItemDto)).thenReturn(itemDto);

        mvc.perform(patch("/items/" + itemDto.getId())
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));

        when(itemService.updateItem(user2.getId(), item.getId(), requestItemDto)).thenThrow(CRUDException.class);

        mvc.perform(patch("/items/" + itemDto.getId())
                        .header(HEADER_USER, user2.getId())
                        .content(mapper.writeValueAsString(requestItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemService, times(1)).updateItem(user.getId(), item.getId(), requestItemDto);
        verify(itemService, times(1)).updateItem(user2.getId(), item.getId(), requestItemDto);
    }

    @Test
    @DisplayName("Удалить вещь")
    public void shouldDeleteItem() throws Exception {
        User user = TestData.getUser();
        Item item = TestData.getItem();

        mvc.perform(delete("/items/" + item.getId())
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().is2xxSuccessful());

        verify(itemService, times(1)).deleteItem(user.getId(), item.getId());
    }

    @Test
    @DisplayName("Создать комментарий")
    public void shouldCreateComment() throws Exception {
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        Item item2 = TestData.getItem2();
        RequestCommentDto requestCommentDto = new RequestCommentDto("new comment");
        CommentDto commentDto = new CommentDto(null, requestCommentDto.getText(), user.getName(), null);

        when(itemService.createComment(user.getId(), item.getId(), requestCommentDto)).thenReturn(commentDto);

        mvc.perform(post("/items/" + item.getId() + "/comment")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));

        when(itemService.createComment(user.getId(), item2.getId(), requestCommentDto)).thenThrow(ChekParamException.class);

        mvc.perform(post("/items/" + item2.getId() + "/comment")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(itemService, times(1)).createComment(user.getId(), item.getId(), requestCommentDto);
        verify(itemService, times(1)).createComment(user.getId(), item2.getId(), requestCommentDto);
    }
}
