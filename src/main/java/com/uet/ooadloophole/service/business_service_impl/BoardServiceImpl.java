package com.uet.ooadloophole.service.business_service_impl;

import com.uet.ooadloophole.database.group_repositories.BoardRepository;
import com.uet.ooadloophole.database.group_repositories.TaskRepository;
import com.uet.ooadloophole.model.business.group_elements.Board;
import com.uet.ooadloophole.model.business.group_elements.Task;
import com.uet.ooadloophole.service.business_service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Board getBoardById(String id) {
        return boardRepository.findBy_id(id);
    }

    @Override
    public List<Board> getBoardByGroup(String groupId) {
        return boardRepository.findByGroupId(groupId);
    }

    @Override
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Task createTask(Task task) {
        Task savedTask = taskRepository.save(task);
        String boardId = task.getBoardId();
        Board board = getBoardById(boardId);
        List<Task> tasks = board.getTasks();
        tasks.add(savedTask);
        boardRepository.save(board);
        return savedTask;
    }
}
