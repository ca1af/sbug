var slideIndex = 1;

showSlide(slideIndex);

function plusSlides(num){
    showSlide(slideIndex += num);
}

function showSlide(num){
    var i;
    var slide = $('.slide');
// 1.이미지 전체 하이드    
    slide.hide();
    
    if(num > slide.length){
        slideIndex = 1;
    }
    if(num < 1){
        slideIndex = slide.length;
    }
    $('a').on('click',function(e){
        e.preventDefault();
    });
    $('.slide').eq(slideIndex-1).show();

}