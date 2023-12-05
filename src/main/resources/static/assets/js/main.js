(function ($) {
    "use strict";

    /*****************************
     * Commons Variables
     *****************************/
    var $window = $(window),
        $body = $('body');

    /****************************
     * Sticky Menu
     *****************************/
    $(window).on('scroll', function () {
        var scroll = $(window).scrollTop();
        if (scroll < 100) {
            $(".sticky-header").removeClass("sticky");
        } else {
            $(".sticky-header").addClass("sticky");
        }
    });

    $(window).on('scroll', function () {
        var scroll = $(window).scrollTop();
        if (scroll < 100) {
            $(".seperate-sticky-bar").removeClass("sticky");
        } else {
            $(".seperate-sticky-bar").addClass("sticky");
        }
    });

    /************************************************
     * Modal Search
     ***********************************************/
    $('a[href="#search"]').on('click', function (event) {
        event.preventDefault();
        $('#search').addClass('open');
        $('#search > form > input[type="search"]').focus();
    });

    $('#search, #search button.close').on('click', function (event) {
        if (event.target.className == 'close') {
            $(this).removeClass('open');
        }
    });

    /*****************************
     * Off Canvas Function
     *****************************/
    (function () {
        var $offCanvasToggle = $('.offcanvas-toggle'),
            $offCanvas = $('.offcanvas'),
            $offCanvasOverlay = $('.offcanvas-overlay'),
            $mobileMenuToggle = $('.mobile-menu-toggle');
        $offCanvasToggle.on('click', function (e) {
            e.preventDefault();
            var $this = $(this),
                $target = $this.attr('href');
            $body.addClass('offcanvas-open');
            $($target).addClass('offcanvas-open');
            $offCanvasOverlay.fadeIn();
            if ($this.parent().hasClass('mobile-menu-toggle')) {
                $this.addClass('close');
            }
        });
        $('.offcanvas-close, .offcanvas-overlay').on('click', function (e) {
            e.preventDefault();
            $body.removeClass('offcanvas-open');
            $offCanvas.removeClass('offcanvas-open');
            $offCanvasOverlay.fadeOut();
            $mobileMenuToggle.find('a').removeClass('close');
        });
    })();


    /**************************
     * Offcanvas: Menu Content
     **************************/
    function mobileOffCanvasMenu() {
        var $offCanvasNav = $('.offcanvas-menu'),
            $offCanvasNavSubMenu = $offCanvasNav.find('.mobile-sub-menu');

        /*Add Toggle Button With Off Canvas Sub Menu*/
        $offCanvasNavSubMenu.parent().prepend('<div class="offcanvas-menu-expand"></div>');

        /*Category Sub Menu Toggle*/
        $offCanvasNav.on('click', 'li a, .offcanvas-menu-expand', function (e) {
            var $this = $(this);
            if ($this.attr('href') === '#' || $this.hasClass('offcanvas-menu-expand')) {
                e.preventDefault();
                if ($this.siblings('ul:visible').length) {
                    $this.parent('li').removeClass('active');
                    $this.siblings('ul').slideUp();
                    $this.parent('li').find('li').removeClass('active');
                    $this.parent('li').find('ul:visible').slideUp();
                } else {
                    $this.parent('li').addClass('active');
                    $this.closest('li').siblings('li').removeClass('active').find('li').removeClass('active');
                    $this.closest('li').siblings('li').find('ul:visible').slideUp();
                    $this.siblings('ul').slideDown();
                }
            }
        });
    }

    mobileOffCanvasMenu();

    /************************************************
     * Nice Select
     ***********************************************/
    // $('select').niceSelect();


    /*************************
     *   Hero Slider Active
     **************************/
    var heroSlider = new Swiper('.hero-slider-active.swiper-container', {
        slidesPerView: 1,
        effect: "fade",
        speed: 1500,
        watchSlidesProgress: true,
        loop: true,
        autoplay: false,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
    });


    /****************************************
     *   Product Slider Active - 4 Grid 2 Rows
     *****************************************/
    var productSlider4grid2row = new Swiper('.product-default-slider-4grid-2row.swiper-container', {
        slidesPerView: 4,
        spaceBetween: 30,
        speed: 1500,
        slidesPerColumn: 2,
        slidesPerColumnFill: 'row',

        navigation: {
            nextEl: '.product-slider-default-2rows .swiper-button-next',
            prevEl: '.product-slider-default-2rows .swiper-button-prev',
        },

        breakpoints: {

            0: {
                slidesPerView: 1,
            },
            576: {
                slidesPerView: 2,
            },
            768: {
                slidesPerView: 2,
            },
            992: {
                slidesPerView: 3,
            },
            1200: {
                slidesPerView: 4,
            }
        }
    });


    /*********************************************
     *   Product Slider Active - 4 Grid Single Rows
     **********************************************/
    var productSlider4grid1row = new Swiper('.product-default-slider-4grid-1row.swiper-container', {
        slidesPerView: 4,
        spaceBetween: 30,
        speed: 1500,

        navigation: {
            nextEl: '.product-slider-default-1row .swiper-button-next',
            prevEl: '.product-slider-default-1row .swiper-button-prev',
        },

        breakpoints: {

            0: {
                slidesPerView: 1,
            },
            576: {
                slidesPerView: 2,
            },
            768: {
                slidesPerView: 2,
            },
            992: {
                slidesPerView: 3,
            },
            1200: {
                slidesPerView: 4,
            }
        }
    });

    /*********************************************
     *   Product Slider Active - 4 Grid Single 3Rows
     **********************************************/
    var productSliderListview4grid3row = new Swiper('.product-listview-slider-4grid-3rows.swiper-container', {
        slidesPerView: 4,
        spaceBetween: 30,
        speed: 1500,
        slidesPerColumn: 3,
        slidesPerColumnFill: 'row',

        navigation: {
            nextEl: '.product-list-slider-3rows .swiper-button-next',
            prevEl: '.product-list-slider-3rows .swiper-button-prev',
        },

        breakpoints: {

            0: {
                slidesPerView: 1,
            },
            640: {
                slidesPerView: 2,
            },
            768: {
                slidesPerView: 2,
            },
            992: {
                slidesPerView: 3,
            },
            1200: {
                slidesPerView: 4,
            }
        }
    });


    /*********************************************
     *   Blog Slider Active - 3 Grid Single Rows
     **********************************************/
    var blogSlider = new Swiper('.blog-slider.swiper-container', {
        slidesPerView: 3,
        spaceBetween: 30,
        speed: 1500,

        navigation: {
            nextEl: '.blog-default-slider .swiper-button-next',
            prevEl: '.blog-default-slider .swiper-button-prev',
        },
        breakpoints: {

            0: {
                slidesPerView: 1,
            },
            576: {
                slidesPerView: 2,
            },
            768: {
                slidesPerView: 2,
            },
            992: {
                slidesPerView: 3,
            },
        }
    });


    /*********************************************
     *   Company Logo Slider Active - 7 Grid Single Rows
     **********************************************/
    var companyLogoSlider = new Swiper('.company-logo-slider.swiper-container', {
        slidesPerView: 7,
        speed: 1500,

        navigation: {
            nextEl: '.company-logo-slider .swiper-button-next',
            prevEl: '.company-logo-slider .swiper-button-prev',
        },
        breakpoints: {

            0: {
                slidesPerView: 1,
            },
            480: {
                slidesPerView: 2,
            },
            768: {
                slidesPerView: 3,
            },
            992: {
                slidesPerView: 3,
            },
            1200: {
                slidesPerView: 7,
            },
        }
    });

    /********************************
     *  Product Gallery - Horizontal View
     **********************************/
    var galleryThumbsHorizontal = new Swiper('.product-image-thumb-horizontal.swiper-container', {
        loop: true,
        speed: 1000,
        spaceBetween: 25,
        slidesPerView: 4,
        freeMode: true,
        watchSlidesVisibility: true,
        watchSlidesProgress: true,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },

    });

    var gallerylargeHorizonatal = new Swiper('.product-large-image-horaizontal.swiper-container', {
        slidesPerView: 1,
        speed: 1500,
        thumbs: {
            swiper: galleryThumbsHorizontal
        }
    });

    /********************************
     *  Product Gallery - Vertical View
     **********************************/
    var galleryThumbsvartical = new Swiper('.product-image-thumb-vartical.swiper-container', {
        direction: 'vertical',
        centeredSlidesBounds: true,
        slidesPerView: 4,
        watchOverflow: true,
        watchSlidesVisibility: true,
        watchSlidesProgress: true,
        spaceBetween: 25,
        freeMode: true,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },

    });

    var gallerylargeVartical = new Swiper('.product-large-image-vartical.swiper-container', {
        slidesPerView: 1,
        speed: 1500,
        thumbs: {
            swiper: galleryThumbsvartical
        }
    });

    /********************************
     *  Product Gallery - Single Slide View
     * *********************************/
    var singleSlide = new Swiper('.product-image-single-slide.swiper-container', {
        loop: true,
        speed: 1000,
        spaceBetween: 25,
        slidesPerView: 4,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },

        breakpoints: {
            0: {
                slidesPerView: 1,
            },
            576: {
                slidesPerView: 2,
            },
            768: {
                slidesPerView: 3,
            },
            992: {
                slidesPerView: 4,
            },
            1200: {
                slidesPerView: 4,
            },
        }

    });

    /******************************************************
     * Quickview Product Gallery - Horizontal
     ******************************************************/
    var modalGalleryThumbs = new Swiper('.modal-product-image-thumb', {
        spaceBetween: 10,
        slidesPerView: 4,
        freeMode: true,
        watchSlidesVisibility: true,
        watchSlidesProgress: true,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
    });

    var modalGalleryTop = new Swiper('.modal-product-image-large', {
        thumbs: {
            swiper: modalGalleryThumbs
        }
    });

    /********************************
     * Blog List Slider - Single Slide
     * *********************************/
    var blogListSLider = new Swiper('.blog-list-slider.swiper-container', {
        loop: true,
        speed: 1000,
        slidesPerView: 1,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },

    });

    /********************************
     *  Product Gallery - Image Zoom
     **********************************/
    $('.zoom-image-hover').zoom();


    /************************************************
     * Price Slider
     ***********************************************/
    $("#slider-range").slider({
        range: true,
        min: 0,
        max: 500,
        values: [75, 300],
        slide: function(event, ui) {
            $("#amount").val("$" + ui.values[0] + " - $" + ui.values[1]);
        }
    });
    $("#amount").val("$" + $("#slider-range").slider("values", 0) +
        " - $" + $("#slider-range").slider("values", 1));


    /************************************************
     * Animate on Scroll
     ***********************************************/
    AOS.init({

        duration: 1000,
        once: true,
        easing: 'ease',
    });
    window.addEventListener('load', AOS.refresh);

    /************************************************
     * Video  Popup
     ***********************************************/
    $('.video-play-btn').venobox();

    /************************************************
     * Scroll Top
     ***********************************************/
    $('body').materialScrollTop();


})(jQuery);

// modal addToCart
function openModal() {
    var modal = document.getElementById("modalAddcart");
    modal.style.display = "block";
}
function updateCartItemCount() {
    $.get("/cart/get-cart-item-count", function (cartItemCount) {
        $(".item-count").text(cartItemCount);
    });
}
// Lắng nghe sự kiện khi người dùng nhấn vào nút "Add to Cart"
document.querySelectorAll(".add-to-cart").forEach(function (addToCartButton) {
    addToCartButton.addEventListener("click", function (e) {
        e.preventDefault(); // Ngăn chặn điều hướng mặc định

        // Lấy product id từ data-product-id
        var productId = this.getAttribute("data-product-id");

        // Kiểm tra số lượng sản phẩm trước khi thêm vào giỏ hàng
        var checkQuantityUrl = "/giohangchitiet/check-quantity/" + productId;
        $.get(checkQuantityUrl, function (data) {
            if (data.success) {
                if (data.quantity === 0) {
                    // Sản phẩm đã hết hàng
                    let type = 'error';
                    let icon = 'fa-solid fa-circle-exclamation';
                    let title = 'Hết hàng!';
                    let text = "Sản phẩm đã hết hàng.";
                    createToast(type, icon, title, text);
                } else if (data.maxQuantityReached) {
                    // Nếu giỏ hàng có số lượng bằng số lượng sản phẩm
                    let type = 'error';
                    let icon = 'fa-solid fa-exclamation-triangle';
                    let title = 'Số lượng giới hạn!';
                    let text = "Số lượng sản phẩm trong giỏ hàng đã đạt đến giới hạn.";
                    createToast(type, icon, title, text);
                } else {
                    // Tiếp tục thêm vào giỏ hàng nếu số lượng > 0
                    var addToCartUrl = "/cart/add-to-cart/" + productId;
                    $.get(addToCartUrl, function (data) {
                        if (data.includes("redirect")) {
                            // Nếu phản hồi chứa từ khóa "redirect", đồng nghĩa với việc chuyển hướng
                            window.location.href = data.split(":")[1].trim();
                        } else {
                            // Nếu không có "redirect", xử lý phản hồi khác như thông thường
                            let type = 'success';
                            let icon = 'fa-solid fa-check-circle';
                            let title = 'Thành công!';
                            let text = "Đã thêm vào giỏ hàng.";
                            // Cập nhật số lượng sản phẩm trong giỏ hàng
                            $.get("/cart/get-cart-item-count", function (cartItemCount) {
                                $(".item-count").text(cartItemCount);
                            });
                            createToast(type, icon, title, text);
                        }
                    });
                }

            } else {
                let type = 'error';
                let icon = 'fa-solid fa-circle-exclamation';
                let title = 'Lỗi!';
                let text = data.message || "Kiểm tra số lượng sản phẩm thất bại.";
                createToast(type, icon, title, text);
            }
        });
        $.get("/cart/get-cart-item-count", function (cartItemCount) {
            $(".item-count").text(cartItemCount);
        });
    });
});


//filter shop product
document.addEventListener("DOMContentLoaded", function () {
    var filterInputs = document.querySelectorAll('input[data-name]');
    var sortSelect = document.getElementById('sortField');
    var timeout;

    filterInputs.forEach(function (input) {
        input.addEventListener("change", function () {
            // Hủy bỏ độ trễ hiện có nếu có
            if (timeout) {
                clearTimeout(timeout);
            }

            // Đặt một độ trễ mới 2 giây trước khi gửi yêu cầu lọc
            timeout = setTimeout(submitForms, 0); // 2 giây
        });
    });

    sortSelect.addEventListener("change", function () {
        // Hủy bỏ độ trễ hiện có nếu có
        if (timeout) {
            clearTimeout(timeout);
        }
        // Đặt một độ trễ mới 2 giây trước khi gửi yêu cầu lọc
        timeout = setTimeout(submitForms, 0); // 2 giây
    });

    function submitForms() {
        document.getElementById("filterForm").submit();
    }
    // Gọi hàm lướt xuống phần tử có class "shop-section" ngay sau khi trang tải xong
    scrollToShopSection();
});

// Hàm lướt xuống phần tử có class "shop-section"
function scrollToShopSection() {
    var shopSection = document.querySelector('.shop-section');
    if (shopSection) {
        shopSection.scrollIntoView({behavior: 'smooth'});
    }
}
