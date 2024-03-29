/**
 * 
 */
window.addEventListener('DOMContentLoaded', () => {
	readAllReplies(); // 모든 댓글 출력
	
	const writer = document.querySelector('#writer');
	
	
	
	function readAllReplies() {
		const reviewId = document.querySelector('#reviewId').value;
		
		axios
			.get('/api/reply/all/' + reviewId)
			.then(response => { updateReplyList(response.data) })
			.catch(err => { console.log(err) })
	}
	
	function updateReplyList(data) {
			const divReplies = document.querySelector('#replies');
			let str = '';
			for (let r of data) {
				str += '<div style="border-top: thin solid silver; padding: 30px 10px;">'
					+ '<div style="font-size: x-small; color: gray; margin-bottom: 5px;">'
					+ '<span>' + r.writer + '</span>'
					+ '</div>'
					+ `<input class="replyInputs" style="width: 90%; font-size: 15px; outline: none; border: none;" value="${r.replyText}" readonly>`
				if (r.writer == writer.value) {
					str += `<button style="float: right;" class="btnDeletes" data-rid="${r.replyId}">삭제</button>`
						+ `<button style="float: right;" class="btnModifies" data-rid="${r.replyId}">수정</button>`
				}
				str += '</div>'
			}
			divReplies.innerHTML = str;
			
			const btnModifies = document.querySelectorAll('.btnModifies');
			const btnDeletes = document.querySelectorAll('.btnDeletes');
			
			btnModifies.forEach(btn => {
				btn.addEventListener('click', modifyReply);
			})
			
			btnDeletes.forEach(btn => {
				btn.addEventListener('click', deleteReply);
			})
	}
	
	function modifyReply(event) {
		// (TODO)현재 페이지에서 수정할지 모달창을 통해 수정할지 
		const rid = event.target.getAttribute('data-rid');
		const modifyDiv = document.querySelector('#modifyDiv');
		alert(rid);
		let str = '<div><input type="text"></div>';
		modifyDiv.innerHTML = str;
	}
	
	function deleteReply(event) {
		const rid = event.target.getAttribute('data-rid');
		
		const result = confirm('삭제하시겠습니까?');
		if (result) {
			axios
			.delete('/api/reply/' + rid)
			.then(response => {
				readAllReplies();
				document.querySelector('#countReply').innerText = response.data;
			})
			.catch(err => { console.log(err) })
		}
		
	}
	
	const btnReply = document.querySelector('#btnReply');
	
	if (btnReply) {
		btnReply.addEventListener('click', registerNewReply);
		
		function registerNewReply() {
			const reviewId = document.querySelector('#reviewId').value;
			const writer = document.querySelector('#writer').value;
			const replyText = document.querySelector('#replyText').value;
			
			if (replyText == '') {
				alert('댓글을 입력해주세요!');
				return;
			}
			
			const data = {
				reviewId: reviewId,
				writer: writer,
				replyText: replyText
			}
			
			axios.post('/api/reply', data)
				.then(response => {
					console.log(response);
					clearInputs(response.data);
					readAllReplies();
				})
				.catch(error => {
					console.log(error);
				})
		}
		
		function clearInputs(data) {
			document.querySelector('#replyText').value = '';
			document.querySelector('#countReply').innerText = data;
		}
		
		
		
		
	}
});