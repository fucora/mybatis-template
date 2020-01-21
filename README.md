mybatis 语法模板语言

> public static SqlWithParam generate(String sqlInXml, Map<String,Object> parameter)
  支持mybatis 语法 返回 待？号语句和 参数
  
> 扩展支持： 语法 其实就是替换成#{} 不建议使用 会略微减少性能

> 自定义语法扩展 
  {@and a.create_time = create_time}  if null 判断 
  {@and a.create_time =,= create_time}    可变条件查询 比如 create_time_like  ("create_time_gt","");
  {@and a.create_time =,like create_time}  可变条件查询 只允许=号和 like 操作