webpackJsonp([1],{

/***/ 188:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_react__ = __webpack_require__(16);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_react___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_react__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_react_dom__ = __webpack_require__(35);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_react_dom___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_react_dom__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__jsx_DivPageTitle_jsx__ = __webpack_require__(84);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__jsx_Chapter_jsx__ = __webpack_require__(189);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_jquery__ = __webpack_require__(33);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_jquery___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_jquery__);





__WEBPACK_IMPORTED_MODULE_1_react_dom___default.a.render(__WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(__WEBPACK_IMPORTED_MODULE_2__jsx_DivPageTitle_jsx__["a" /* default */], { title: '\u7AE0\u8282\u9875\u9762' }), document.getElementById('page-title'));
__WEBPACK_IMPORTED_MODULE_4_jquery___default.a.get('/book/chapter/1/10', {}, function (data) {
  __WEBPACK_IMPORTED_MODULE_1_react_dom___default.a.render(__WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(__WEBPACK_IMPORTED_MODULE_3__jsx_Chapter_jsx__["a" /* default */], { chapter: data }), document.getElementById('main-ui'));
});

/***/ }),

/***/ 189:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_react__ = __webpack_require__(16);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_react___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_react__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_jquery__ = __webpack_require__(33);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_jquery___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_jquery__);
var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }




var ChapterButton = function (_React$Component) {
  _inherits(ChapterButton, _React$Component);

  function ChapterButton() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, ChapterButton);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = ChapterButton.__proto__ || Object.getPrototypeOf(ChapterButton)).call.apply(_ref, [this].concat(args))), _this), _this.handleClick = function (index) {
      _this.props.call(index);
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(ChapterButton, [{
    key: 'render',
    value: function render() {
      var divStyle = {
        display: 'flex',
        justifyContent: 'center'
      };
      var itemStyle = {
        display: 'flex'
      };
      var spanStyle = {
        marginLeft: 10,
        marginRight: 10
      };
      return __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
        'div',
        { style: divStyle },
        __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
          'span',
          { style: itemStyle },
          __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
            'span',
            { style: spanStyle, onClick: this.handleClick.bind(this, this.props.index - 1) },
            __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
              'a',
              { href: 'javascript:;' },
              '\u4E0A\u4E00\u9875'
            )
          ),
          __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
            'span',
            { style: spanStyle },
            '\u76EE\u5F55'
          ),
          __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
            'span',
            { style: spanStyle, onClick: this.handleClick.bind(this, this.props.index + 1) },
            __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
              'a',
              { href: 'javascript:;' },
              '\u4E0B\u4E00\u9875'
            )
          )
        )
      );
    }
  }]);

  return ChapterButton;
}(__WEBPACK_IMPORTED_MODULE_0_react___default.a.Component);

var Chapter = function (_React$Component2) {
  _inherits(Chapter, _React$Component2);

  function Chapter(props) {
    _classCallCheck(this, Chapter);

    var _this2 = _possibleConstructorReturn(this, (Chapter.__proto__ || Object.getPrototypeOf(Chapter)).call(this, props));

    _this2.handleCall = function (index) {
      var newURL = '/book/chapter/' + _this2.state.chapter.bookId + '/' + index;
      var self = _this2;
      __WEBPACK_IMPORTED_MODULE_1_jquery___default.a.get(newURL, {}, function (data) {
        self.setState({ chapter: data });
      });
    };

    _this2.state = _this2.props;
    return _this2;
  }

  _createClass(Chapter, [{
    key: 'render',
    value: function render() {
      var rawHTML = {
        __html: this.state.chapter.content.replace(/\n/g, "<br />")
      };
      return __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
        'div',
        null,
        __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
          'div',
          null,
          this.state.chapter.title
        ),
        __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement('div', { dangerouslySetInnerHTML: rawHTML }),
        __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(
          'div',
          null,
          __WEBPACK_IMPORTED_MODULE_0_react___default.a.createElement(ChapterButton, { index: this.state.chapter.index, call: this.handleCall })
        )
      );
    }
  }]);

  return Chapter;
}(__WEBPACK_IMPORTED_MODULE_0_react___default.a.Component);

/* harmony default export */ __webpack_exports__["a"] = (Chapter);

/***/ })

},[188]);