package com.br.uellisson.controleelevador.model;


public class UserUI {
    public static String PROVIDER = "com.br.uellisson.controleelevador.model.User.PROVIDER";


    private String email;
    private String id;
    private String name;
    private String password;


    public UserUI(){}

    public UserUI(String email, String id, String name, String password) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public static String getPROVIDER() {
        return PROVIDER;
    }

    public static void setPROVIDER(String PROVIDER) {
        UserUI.PROVIDER = PROVIDER;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public boolean isSocialNetworkLogged( Context context ){
//        String token = getProviderSP( context );
//        return( token.contains("facebook") || token.contains("google") || token.contains("twitter") || token.contains("github") );
//    }
//
//
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    private void setNameInMap( Map<String, Object> map ) {
//        if( getName() != null ){
//            map.put( "name", getName() );
//        }
//    }
//
//    public void setNameIfNull(String name) {
//        if( this.name == null ){
//            this.name = name;
//        }
//    }
//
//
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    private void setEmailInMap( Map<String, Object> map ) {
//        if( getEmail() != null ){
//            map.put( "email", getEmail() );
//        }
//    }
//
//    public void setEmailIfNull(String email) {
//        if( this.email == null ){
//            this.email = email;
//        }
//
//    }
//
//
//    @Exclude
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//
//
//    @Exclude
//    public String getNewPassword() {
//        return newPassword;
//    }
//
//    public void setNewPassword(String newPassword) {
//        this.newPassword = newPassword;
//    }
//
//
//
//
//    public void saveProviderSP(Context context, String token ){
//        LibraryClass.saveSP( context, PROVIDER, token );
//    }
//    public String getProviderSP(Context context ){
//        return( LibraryClass.getSP( context, PROVIDER) );
//    }
//
//
//
//    public void saveDB( DatabaseReference.CompletionListener... completionListener ){
//        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );
//
//        if( completionListener.length == 0 ){
//            firebase.setValue(this);
//        }
//        else{
//            firebase.setValue(this, completionListener[0]);
//        }
//    }
//
//    public void updateDB( DatabaseReference.CompletionListener... completionListener ){
//
//        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );
//
//        Map<String, Object> map = new HashMap<>();
//        setNameInMap(map);
//        setEmailInMap(map);
//
//        if( map.isEmpty() ){
//            return;
//        }
//
//        if( completionListener.length > 0 ){
//            firebase.updateChildren(map, completionListener[0]);
//        }
//        else{
//            firebase.updateChildren(map);
//        }
//    }
//
//    public void removeDB( DatabaseReference.CompletionListener completionListener ){
//
//        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );
//        firebase.setValue(null, completionListener);
//    }
//
//    public void contextDataDB( Context context ){
//        DatabaseReference firebase = LibraryClass.getFirebase().child("users").child( getId() );
//
//        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
//    }
}