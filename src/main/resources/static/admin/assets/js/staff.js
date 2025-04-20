// Staff Management Functions
const StaffManager = {
    // Hàm mở modal
    openModal: function(modalId) {
        document.getElementById(modalId).classList.add('show');
        document.body.style.overflow = 'hidden';
    },

    // Hàm đóng modal
    closeModal: function(modalId) {
        document.getElementById(modalId).classList.remove('show');
        document.body.style.overflow = '';
    },

    // Hàm chỉnh sửa nhân viên
    editStaff: function(id) {
        fetch(`/staff/${id}`)
            .then(response => response.json())
            .then(staff => {
                document.getElementById('editId').value = staff.id;
                document.getElementById('editCode').value = staff.code;
                document.getElementById('editName').value = staff.name;
                document.getElementById('editFptEmail').value = staff.fptEmail;
                document.getElementById('editFeEmail').value = staff.feEmail;
                
                this.openModal('editStaffModal');
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi lấy thông tin nhân viên');
            });
    },

    // Hàm toggle trạng thái
    toggleStatus: function(id) {
        if (confirm('Bạn có chắc chắn muốn thay đổi trạng thái nhân viên này?')) {
            fetch(`/staff/${id}/toggle-status`, {
                method: 'PUT'
            })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    throw new Error('Network response was not ok');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi thay đổi trạng thái nhân viên');
            });
        }
    },

    // Khởi tạo các event handlers
    init: function() {
        // Xử lý nút thêm nhân viên
        const addStaffBtn = document.getElementById('addStaffBtn');
        if (addStaffBtn) {
            addStaffBtn.addEventListener('click', () => this.openModal('addStaffModal'));
        }

        // Xử lý nút chỉnh sửa nhân viên
        const editButtons = document.querySelectorAll('.edit-staff-btn');
        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const id = button.getAttribute('data-id');
                this.editStaff(id);
            });
        });

        // Xử lý nút toggle status
        const toggleButtons = document.querySelectorAll('.toggle-status-btn');
        toggleButtons.forEach(button => {
            button.addEventListener('click', () => {
                const id = button.getAttribute('data-id');
                this.toggleStatus(id);
            });
        });

        // Xử lý form thêm nhân viên
        const addStaffForm = document.getElementById('addStaffForm');
        if (addStaffForm) {
            addStaffForm.addEventListener('submit', function(e) {
                e.preventDefault();
                const formData = new FormData(this);
                const data = Object.fromEntries(formData.entries());
                
                fetch('/staff/ajax', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        alert(data.error);
                    } else {
                        location.reload();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi thêm nhân viên');
                });
            });
        }

        // Xử lý form chỉnh sửa nhân viên
        const editStaffForm = document.getElementById('editStaffForm');
        if (editStaffForm) {
            editStaffForm.addEventListener('submit', function(e) {
                e.preventDefault();
                const formData = new FormData(this);
                const data = Object.fromEntries(formData.entries());
                const id = data.id;
                
                fetch(`/staff/${id}/ajax`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        alert(data.error);
                    } else {
                        location.reload();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi cập nhật nhân viên');
                });
            });
        }

        // Đóng modal khi click ra ngoài
        window.onclick = (event) => {
            if (event.target.classList.contains('custom-modal')) {
                this.closeModal(event.target.id);
            }
        };
    }
};

// Khởi tạo khi DOM đã load
document.addEventListener('DOMContentLoaded', () => StaffManager.init()); 