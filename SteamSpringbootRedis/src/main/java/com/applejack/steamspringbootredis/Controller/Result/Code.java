package com.applejack.steamspringbootredis.Controller.Result;

/**
 * 枚举在json化的时候，会转换成字符串<br>
 * A1:业务完成，只有状态码。A2:业务完成，有状态码和返回值。
 * A3:业务完成，有状态码、返回值、提示信息<br>
 * B1:业务失败，只有状态码。B2:业务失败，有状态码和提示信息。
 * B3:业务失败，有状态码、返回值、提示信息
 * @author Akemi0Homura
 */
public enum Code {
    A1,A2,A3,
    B1,B2,B3;
}
