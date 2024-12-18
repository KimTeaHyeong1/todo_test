const dday = document.querySelector('form');
const ddayForm = document.querySelector('.d-day-form');
const profile = document.querySelector('.profile');

const imageUploadBox = document.querySelector('.image-upload-box');
const fileUploader = document.querySelector('#imageUploader');

const nameInput = document.querySelector('#name');
const yearInput = document.querySelector('#year');
const monthInput = document.querySelector('#month');
const dayInput = document.querySelector('#day');
const hourInput = document.querySelector('#hour');
const minuteInput = document.querySelector('#minute');
const submitButton = document.querySelector('#submitButton');

const profileImage = document.querySelector('#image');
const nameText = document.querySelector('.name-text');
const birthdayText = document.querySelector('.birthday');
const timeText = document.querySelector('.time');
const yearText = document.querySelector('.year');
const monthText = document.querySelector('.month');
const dayText = document.querySelector('.day');
const hourText = document.querySelector('.hour');
const minuteText = document.querySelector('.minute');

let fileUrl;

// 이미지 업로드 박스를 클릭하면 파일 업로드 창이 열림
imageUploadBox.addEventListener('click', function() {
    fileUploader.click();
});

// 파일을 선택했을 때 처리
fileUploader.addEventListener('change', function() {
    // 1. 이미지 태그를 생성
    const imageTag = document.createElement('img');
    // 2. 선택한 파일
    const selectedFile = fileUploader.files[0];
    // 3. 선택한 파일을 URL로 변환
    fileUrl = URL.createObjectURL(selectedFile);
    // 4. 이미지 태그의 src 속성에 파일 URL을 설정
    imageTag.setAttribute('src', fileUrl);
    // 5. 기존의 내용은 삭제하고 새 이미지를 추가
    imageUploadBox.innerHTML = '';
    imageUploadBox.append(imageTag);
});

// 폼 제출 시 동작
dday.addEventListener('submit', function(event) {
    event.preventDefault();

    ddayForm.classList.add('hide');  // 폼을 숨기기
    profile.classList.remove('hide');  // 프로필 화면 보이기

    // 업로드한 이미지를 프로필에 추가
    profileImage.setAttribute('src', fileUrl);
    // 입력된 이름, 생일, 시간을 프로필에 표시
    nameText.textContent = nameInput.value;
    birthdayText.textContent = `${yearInput.value}년 ${monthInput.value}월 ${dayInput.value}일`;
    timeText.textContent = `${hourInput.value}시 ${minuteInput.value}분`;
});
