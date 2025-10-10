function createNotification() {
  let type = prompt('알림 유형', 'EX) TASK_ASSIGNED');
  let ref_type = prompt('참조 대상 종류', 'EX) TASK OR COMMENT');
  let ref_id = prompt('참조 대상 ID', 'UserId');
  let message = prompt('알림 메시지');
}

document.getElementById('create-btn').addEventListener('click', createNotification);