package com.uet.ooadloophole.service.business_service;

import com.uet.ooadloophole.model.business.group_elements.Board;
import com.uet.ooadloophole.model.business.group_elements.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {
    Board getBoardById(String id);

    List<Board> getBoardByGroup(String groupId);

    Board createBoard(Board board);

    Task createTask(Task task);

}
