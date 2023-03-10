/**
 * 
 */
window.addEventListener('DOMContentLoaded', () => {
	readAllReplies(); // 모든 댓글 출력
	
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
				str += '<div style="border-top: thin solid silver; padding: 20px 10px;">'
					+ '<div style="font-size: small; color: gray; margin-bottom: 5px;">'
					+ '<span>' + r.writer + '</span>'
					+ '</div>'
					+ '<div style="font-size: 15px;">' + r.replyText + '</div>'
					+ '</div>'
			}
			divReplies.innerHTML = str;
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