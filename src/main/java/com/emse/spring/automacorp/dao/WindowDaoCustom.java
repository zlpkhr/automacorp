package com.emse.spring.automacorp.dao;

import com.emse.spring.automacorp.entity.WindowEntity;

import java.util.List;

public interface WindowDaoCustom {
    List<WindowEntity> findRoomsWithOpenWindows(Long id);

    List<WindowEntity> findAllWindowsByRoomName(String roomName);

    void deleteByRoom(Long roomId);
}
