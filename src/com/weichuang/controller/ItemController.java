package com.weichuang.controller;

import com.weichuang.pojo.Item;
import com.weichuang.service.IItemService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Controller :
 * 1、将ItemController  ioc到你的spring容器中（将对象在spring容器中创建出来）
 * 2、标记controller层，前端控制器会在controller层中寻找匹配
 */
@Controller
//限制此控制层，仅对item商品操作
//@RequestMapping("item")
public class ItemController {

    @Autowired
    JdbcTemplate jt;

    @Autowired
    IItemService iItemService;
    //向前端控制器暴露接口
    //此时若设置为仅支持post方式，若get方式访问时会报 Request method 'GET' not supported
    @RequestMapping(value = {"/list.do" , "/itemList.do"} , method = { RequestMethod.GET })
    public ModelAndView getItemList(){
        List<Item> itemList = jt.query("select * from items", new RowMapper<Item>() {

            @Override
            public Item mapRow(ResultSet rs, int i) throws SQLException {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setDetail(rs.getString("detail"));
                item.setPrice(rs.getString("price"));
                item.setCreatetime(rs.getString("createtime"));
                return item;
            }
        });
        //int i  = 1/0;
        //相当于request
        ModelAndView mav = new ModelAndView();
        mav.addObject("itemList" , itemList);//request.setAttribute(key , value);
        mav.setViewName("itemList");
        return mav;
    }

    /**
     * required : 参数是否必须，true表示必须，false表示不必须
     * defaultValue : 参数取不到的时候的默认值
     * 通常defaultValue 与required = false时配合使用
     *
     * 若设定为必传属性时，还没有默认值就会报Required String parameter 'id' is not present
     * @param idxx
     * @param resp
     * @return
     */
    @RequestMapping("/editItem.do")
    public ModelAndView editItem(@RequestParam(value = "id") String idxx , boolean status ,HttpServletRequest resp ){
        System.out.println("=============" + idxx + " ========== status = " +status);
        //String id = resp.getParameter("id");
        Item item = iItemService.getItemById(idxx);
        ModelAndView mav = new ModelAndView();
        mav.addObject("item",item);
        mav.setViewName("editItem");
        return mav;
    }
    @RequestMapping("/updateItem.do")
    public String updateItem(MultipartFile pictureFile , Item item , Model model , boolean status , HttpServletRequest request){

        //1、MultipartFile pictureFile 将表单中的文件接收到
        System.out.println("pictureFile = " + pictureFile);
        //2、获取真实存放的路径
        String realPath = request.getServletContext().getRealPath("pic");
        //3、uuid或日期毫秒化,为文件重新命名，防止重复
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        String newFileName = UUID.randomUUID().toString().replace("-", "");
        //4、获取文件扩展名,下面的方法不带.
        String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());//pictureFile.getOriginalFilename():获取文件的全名
        System.out.println(extension);
        try {
            //5、上传图片到服务器 , new File() : 形参是存储的路径
            pictureFile.transferTo(new File(realPath + "/" + newFileName + "." + extension));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //6、更新到数据库中
        item.setPic(newFileName + "."+ extension);
        int rows = iItemService.updateItem(item);
        System.out.println("======== rows = " + rows);
        if(rows > 0){
            //List<Item> itemList = iItemService.getAllItem();
            model.addAttribute("item" , item);
            return "editItem";
        }else{
            return "editItem";
        }
    }
    @RequestMapping("/deleteItems.do")
    public String deleteItems(String[] ids , Model model){
        System.out.println("========= ids : " + Arrays.toString(ids));
        int rows = iItemService.deleteItems(ids);
        if(rows > 0){
            List<Item> itemList = iItemService.getAllItem();
            model.addAttribute("itemList" , itemList);
        }
        return "itemList";
    }
    //前后端分离项目使用void返回值下面居多
    @RequestMapping("/ajax.do")
    public void ajaxTest(HttpServletRequest request , HttpServletResponse response){
        request.getParameter("");//获取ajax前端数据
        try {
            response.getWriter().write("json");//向ajax写回json
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //@RequestBody ： 将一个json格式转换为对象 -- 了解 ，之后几乎不用。若普通的表单提交的方式，使用此注解会取不到值。
    //@ResponseBody : 将一个对象转换为json格式 。重点
    @RequestMapping("/json.do")
    @ResponseBody
    public Item jsonTest(Item item){
        System.out.println("json item = " + item);
        item.setDetail("ResponseBody响应的");
        return item;
    }
}
